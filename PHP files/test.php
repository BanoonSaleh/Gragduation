<?php
 require_once('dbconfig.php');
$title="alaa";$url="alaa";

$sql_query="insert into result values('$title', '$url');";
 
$result=mysqli_query($con, $sql_query);


?>