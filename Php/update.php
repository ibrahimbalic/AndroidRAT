<?php

require_once('class/inc.php');
$Device = @$_POST["device"];
$task = @$_POST["task"];
$data = @$_POST["data"];
$ty   = @$_GET["q"];

$xY 	= new Agents();	

echo $xY->AgentInsert($Device,$task ,base64_decode($data,true),$ty);


 @header('Pragma: no-cache');
 @header('Cache-Control: no-store, no-cache, max-age=0, must-revalidate');
 @header("Content-type: text/xml");
?>
