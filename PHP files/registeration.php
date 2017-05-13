<?php
 require_once('dbconfig.php');
$Name= $_POST["user"];
$User_Name= $_POST["UserName"];
$User_Email=$_POST["Email"];
$User_Pass=$_POST["Password"];
$User_REPass=$_POST["Re-pass"];

$sql_query="insert into UserInformation values('$Name', '$User_Name','$User_Email','$User_Pass','$User_REPass');";
 if (mysqli_query($con, $sql_query))
{ echo "Data Inserted";}
else
{ echo "Data Insertion Error...".mysql_error($con);}

?>