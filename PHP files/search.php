<?php
//search.php
/*
 * Following code will search idiom based on keywords
 */
 
// array for JSON response
$response = array();
 

require"dbconfig.php";
 

$keyword=$_GET["keyword"];

//using LIKE
$result = mysqli_query($con,"SELECT * FROM record WHERE Title LIKE '%$keyword%' LIMIT 0, 20");
 
// check for empty result
if (mysqli_num_rows($result) > 0) {
    $response["record"] = array();
 
    while ($row = mysqli_fetch_array($result)) {
        // temp user array
        $record= array();
        $record["id"] = $row["id"];
        $record["Title"] = $row["Title"];
        $record["URL"] = $row["URL"];
 
        // push single idiom array into final response array
        array_push($response["record"], $record);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No record found";
 
    // echo no users JSON
    echo json_encode($response);
}
?>