
<?php
    $con = mysqli_connect("localhost", "seatrea", "wldus1037!", "seatrea");
    mysqli_query($con,'SET NAMES utf8');
    
    $userName = $_POST["userName"];
    $userBirth = $_POST["userBirth"];
    $userNumber=$_POST["userNumber"];
	$userID = $_POST["userID"];
    $userState='Infected';
    $is_survey='no';

    $statement = mysqli_prepare($con, "UPDATE `USER` SET `userName` = ? WHERE `USER`.`userID` = ?");
    mysqli_stmt_bind_param($statement, "ss", $userName, $userID);
	mysqli_stmt_execute($statement);
	
	
	$statement = mysqli_prepare($con, "UPDATE `USER` SET `userBirth` = ? WHERE `USER`.`userID` = ?");
    mysqli_stmt_bind_param($statement, "ss", $userBirth, $userID);
    mysqli_stmt_execute($statement);
	
	$statement = mysqli_prepare($con, "UPDATE `USER` SET `userNumber` = ? WHERE `USER`.`userID` = ?");
    mysqli_stmt_bind_param($statement, "ss", $userNumber, $userID);
    mysqli_stmt_execute($statement);
	

    $response = array();
    $response["success"] = true;

    echo json_encode($response);

?>
