<?php
require"dbconfig.php";
$course_Name= $_POST["course_Name"];

$sqlDelete="TRUNCATE Table Result";
$resultDelete= mysqli_query($con, $sqlDelete);

$sql = "SELECT * FROM taught WHERE Title LIKE '%$course_Name' 
        UNION SELECT * FROM taught WHERE Title LIKE '$course_Name%' 
        UNION SELECT * FROM taught WHERE Title LIKE '%$course_Name%' ";

$result= mysqli_query($con, $sql);
if(mysqli_num_rows($result)>0)
{ 

   while($row = $result->fetch_assoc()) 
      {   
        $title=$row["Title"];
        $url= $row["URL"]  ;
        $sqlInsert="INSERT INTO Result Values('$title', '$url')"; 
        $resultInsert= mysqli_query($con, $sqlInsert);
        
      }

}
else { 
     echo "Login Failed No User detail Avaliable";}
echo "done";

?>
