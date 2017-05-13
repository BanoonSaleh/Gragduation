<?php
$db_name="webappdb";
$mysql_user="root";
$mysql_pass="";
$server_name="localhost";
$con= mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name);
if(!$con) {
echo"Connection not eastablished".mysqli_connect_erro(); }
Else {
echo"<h2>Connection Successfully done for the MAP Class of KFU CCSIT......</h2>";  
}  ?>
