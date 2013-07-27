<?php
#/****************************************************************
#\ [INFO]
#/ 
#\
#/  **************************************************************
#\  
#/  Copyright (c) 2011 Balic Computer
#\
#/  @Orginal 	http://www.balicbilisim.com.tr
#\  @File  		Demo 1165 -  Mysql Connector
#/  @Author  	Ibrahim BALIC
#\
#/  -------------------------------	--------------------------
#\   Balic Computer UK		 	 	Baliç Bilişim Türkiye
#/  -------------------------------	--------------------------
#\   ADDRESS:  				 		 ADDRESS:
#/   110 Rushden Gardens   		 	 Hudud yolu cad. no 23/a
#\   Essex, Ilford, IG5 0WB			 Yeni Mah, Pendik, 34893
#/   London - United Kingdom		 Istanbul - Turkiye
#\   
#/   TEL: 		 		 			 Tel: 
#\   +447760975969					 +902164887799
#/  
#\   EMAIL:
#/   info@balicbilisim.com.tr
#\   
#/   WEB:
#\   www.balicbilisim.com
#/   www.balicbilisim.com.tr  
#\  
#/****************************************************************

 class DBConnector extends Configs
 {

  public function __construct(){
   try {
    @mysql_query("SET NAMES ".$this->Charset); 
    @mysql_query("SET CHARACTER SET ".$this->Charset); 
    @mysql_query("SET COLLATION_CONNECTION = '".$this->CharsetT."'"); 
    //return $this->DBCon;
   } 
   catch (Exception $e) {
    echo $e->getMessage();
   } 
  }

  # - Database Query -------------------------------------------- #
  public function Sorgu($Sorgum,$Secim = "0",$FF=null) {
  
   $this->DBCon=mysql_connect( $this->DBServer, $this->DBUser, $this->DBUserPass) or die ('MySQL connect failed. ' .mysql_error());
   mysql_select_db ( $this->DBName, $this->DBCon);
   
   switch($Secim) {
    case "0":
     return mysql_query($Sorgum);
    break;
    case "1":
     $Al= mysql_query($Sorgum);
     return @mysql_fetch_array($Al, MYSQL_NUM);
    break;
    case "2":
     $Al= mysql_query($Sorgum);
     return mysql_num_rows($Al);
    break;
    case "3": 
     $Al = @mysql_list_tables($this->DataBaseAdi);
     return mysql_fetch_array($Al);
    break;
    case "4":
     $Al= @mysql_query($Sorgum);
     return mysql_fetch_assoc($Al);
    break;
    case "5":
     $Al= @mysql_query($Sorgum);
     return mysql_fetch_array($Al);
    break;
    default:
    break;
   }	
  }
  
  # - Database Disconnection ------------------------------------ #
  public function close(){
    @mysql_close($this->DBCon);
  }

  # - Database Error -------------------------------------------- #
  public function error(){
   return mysql_error($this->DBCon);
  }
 }
 
#/****************************************************************
#\ [INFO]
#/ 
#\
#/  **************************************************************
#\  
#/  Copyright (c) 2011 Balic Computer
#\
#/  @Orginal 	http://www.balicbilisim.com.tr
#\  @File  		Demo 1165 -  Mysql Connector
#/  @Author  	Ibrahim BALIC
#\
#/  -------------------------------	--------------------------
#\   Balic Computer UK		 	 	Baliç Bilişim Türkiye
#/  -------------------------------	--------------------------
#\   ADDRESS:  				 		 ADDRESS:
#/   110 Rushden Gardens   		 	 Hudud yolu cad. no 23/a
#\   Essex, Ilford, IG5 0WB			 Yeni Mah, Pendik, 34893
#/   London - United Kingdom		 Istanbul - Turkiye
#\   
#/   TEL: 		 		 			 Tel: 
#\   +447760975969					 +902164887799
#/  
#\   EMAIL:
#/   info@balicbilisim.com.tr
#\   
#/   WEB:
#\   www.balicbilisim.com
#/   www.balicbilisim.com.tr  
#\  
#/****************************************************************
?>