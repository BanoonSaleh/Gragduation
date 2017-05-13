<?php
require"dbconfig.php";
$User_Name=$_POST["login_name"];
$User_Pass=$_POST["login_pass"];


$sql_query= "select Name from UserInformation where UserName like '$User_Name' and Password like '$User_Pass'";
$result= mysqli_query($con, $sql_query);

if(mysqli_num_rows($result)>0)
{ 
$row= mysqli_fetch_assoc($result);    
$name=$row["Name"];
echo "Login Successful Welcome...".$name;
}
else { echo "Login Failed No User detail Avaliable";}

?>
