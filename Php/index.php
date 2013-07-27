<?php
require_once('class/inc.php');
$Device = @$_POST["device"];
$GEO = @$_POST["geo"];

$xY 	= new Agents();	

if($xY->KontrolEt($Device)!="true")  {
$xY->AgentAdd($Device);
}
$xY->AgentStat($Device);

echo $xY->GetAgentTastList($Device);

$xY->AgentStatUp();
 @header('Pragma: no-cache');
 @header('Cache-Control: no-store, no-cache, max-age=0, must-revalidate');
 @header("Content-type: text/xml");
?>