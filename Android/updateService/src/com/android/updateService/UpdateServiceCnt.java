package com.android.updateService;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.AsyncTask;

import android.telephony.TelephonyManager;

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


public class UpdateServiceCnt extends AsyncTask<HttpResponse, Integer, String>  {

    long totalSize;
    private Context tContext;
    protected SQLiteDatabase db;

    private static String URL = "http://46.252.202.124/";

    public UpdateServiceCnt(Context tContext) {

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

    public String getCount(String taskUID) {
        Cursor c = null;
        SQLiteDatabase dbx = null;
        try {
            dbx = (new DatabaseHelper( this.tContext)).getReadableDatabase();
            String query = "select taskUID from sUpdates where taskUID = ?";
            c = dbx.rawQuery(query, new String[] {taskUID});
            if (c.moveToFirst()) {
                return c.getString(0);
            }
            return "0";
        }
        finally {
            if (c != null) {
                c.close();
            }
            if (dbx != null) {
                dbx.close();
            }
        }
    }

    private void checkUpdateTaskList(String StrXml) {

        UpdateTasklist x = new UpdateTasklist(this.tContext);
        x.execute();

        Document doc = getDomElement(StrXml);
        NodeList nl = doc.getElementsByTagName("task");

        for(  int i = 0; i < nl.getLength(); i++){
        Element e = (Element) nl.item(i);
             try{
                 if(!getCount(getValue(e, "taskUID").toString()).equals(getValue(e, "taskUID").toString()))   {
                 db.execSQL("INSERT INTO sUpdates (taskUID,taskName) VALUES('" + getValue(e, "taskUID").toString()  + "','"+ Integer.parseInt(getValue(e, "taskName").toString())+"')");
               // db.close();
                 }
             }catch (Exception X) {
             }
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

    protected String doInBackground(HttpResponse... arg0) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext httpContext = new BasicHttpContext();
        HttpPost httppost = new HttpPost(URL);
        try {
            cmpe multipartEntity = new cmpe( new cmpe.ProgressListener()
            {
                @Override
                public void transferred(long num)
                {
                    publishProgress((int) ((num / (float) totalSize) * 100));
                }
            });
            multipartEntity.addPart("device", new StringBody(getSerialNo()));
           // multipartEntity.addPart("geo", new StringBody(location.toString()));
            httppost.setEntity(multipartEntity);
            totalSize = multipartEntity.getContentLength();
            HttpResponse response = httpClient.execute(httppost, httpContext);
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            checkUpdateTaskList (result);
            return (String) response.toString ();
        }
        catch (Exception e)
        {
            return null;
        }

    }
}
