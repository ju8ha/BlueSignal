
<?php
    $con = mysqli_connect("localhost", "seatrea", "wldus1037!", "seatrea");
    mysqli_query($con,'SET NAMES utf8');
    
    $hostName = $_POST["hostName"];
    $hostNumber=$_POST["hostNumber"];
	$hostID = $_POST["hostID"];

    $statement = mysqli_prepare($con, "UPDATE `HOST` SET `hostName` = ? WHERE `HOST`.`hostID` = ?");
    mysqli_stmt_bind_param($statement, "ss", $hostName, $hostID);
	mysqli_stmt_execute($statement);
	
	$statement = mysqli_prepare($con, "UPDATE `HOST` SET `hostNumber` = ? WHERE `HOST`.`hostID` = ?");
    mysqli_stmt_bind_param($statement, "ss", $hostNumber, $hostID);
    mysqli_stmt_execute($statement);
	

    $response = array();
    $response["success"] = true;

    echo json_encode($response);

?>
