<?php


 class Agents extends DBConnector {
 
 
 	public function getTaskUID($Device,$UID) {
	 try {
	 	$Al = parent::Sorgu("SELECT taskUID from tasklist WHERE deviceId = '".$Device."' and taskUID = '".$UID."' and taskStat='1'","5");
		 if($Al[0]==$UID) {
         return "true";
		 }else{
		 	return "false";
		 } 
	 } catch (Exception $e) {
	   return $e->getMessage();
	 }
	}
 
	public function KontrolEt($IMEI) {
	$Al = parent::Sorgu("SELECT IMEI from agents WHERE IMEI='".$IMEI."'","5");
		if($Al[0]==$IMEI) {
		  return  "true";
			}else{
		  return "false";
		}
	}
	
	public function AgentAdd($IMEI) {
	 parent::Sorgu("INSERT INTO agents ( `IMEI`, `stat`, `signal`, `rip`) VALUES ('".$IMEI."','2',NOW(),'".$_SERVER["REMOTE_ADDR"]."');");
	}
	
 	public function AgentStatUp() {
	 parent::Sorgu("UPDATE agents set stat='1' WHERE stat='2' and DATE_SUB(NOW(), INTERVAL 300 SECOND) >= `signal`");
	}
 

	public function AgentGEOInsert($Device,$data) {
	 try {
		
	 	parent::Sorgu("INSERT INTO geo ( `deviceId`,`data`) VALUES ( '".@$Device."', '".@$data."');","0");
	 } catch (Exception $e) {
	   return $e->getMessage();
	 }
	}

  	public function AgentStat($Device) {
	 	   parent::Sorgu("UPDATE agents SET `stat`='2', `signal`=NOW() WHERE IMEI = '".$Device."'","0");
	}
    public function GetAgentTastList($Agent) {
	try {
		$Al = parent::Sorgu("SELECT  * from tasklist where deviceId = '".$Agent."' and taskStat='1'");
		$ExportXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		 $ExportXML .= "<android>\n";
		while( $Kayitlar 		= mysql_fetch_array ($Al) ) {
			$ExportXML .= " <task>\n";
			$ExportXML .= "  <taskName>". $Kayitlar["taskName"]."</taskName>\n";
			$ExportXML .= "  <taskUID>". (($Kayitlar["taskUID"]==NULL)?'null':$Kayitlar["taskUID"])."</taskUID>\n";
			$ExportXML .= " </task>\n";
		}
	  $ExportXML .= "</android>";

	   return $ExportXML;
	 } catch (Exception $e) {
	   return $e->getMessage();
	 }
	}
	
	public function AgentInsert($Device,$task,$data,$xtype) {
	 try {
		if(self::getTaskUID($Device,$task) == "true") {
		parent::Sorgu("SET character_set_results = 'utf8', character_set_client = 'utf8', character_set_connection = 'utf8', character_set_database = 'utf8', character_set_server = 'utf8'");

	 	parent::Sorgu("INSERT INTO datas ( `deviceID`,`taskUID`,`data`,`type`) VALUES ( '".@$Device."', '".@$task."', '". mysql_real_escape_string( @$data)."','".$xtype."')");
		if(mysql_affected_rows() >= 0) {
			parent::Sorgu("UPDATE tasklist SET taskStat='2' where  deviceId = '".$Device."' and taskUID = '".$task."'");
			if(mysql_affected_rows() >= 0) {
			  $ExportXML = '<result>
						<stat>true</stat>
						<taskUID>'.$task.'</taskUID>
					</result>';
				return $ExportXML;
			}else{
				$ExportXML = '<result>
						<stat>false</stat>
						<taskUID>'.$task.'</taskUID>
					</result>';
				return $ExportXML;
			
			}
		 }
		}
	 } catch (Exception $e) {
	   return $e->getMessage();
	 }
	}
}


?>