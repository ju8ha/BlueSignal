
<?php
    $con = mysqli_connect("localhost", "seatrea", "wldus1037!", "seatrea");
    mysqli_query($con,'SET NAMES utf8');


    $userID = $_POST["userID"];
    $userPassword = $_POST["userPassword"];
    $userName = $_POST["userName"];
    $userBirth = $_POST["userBirth"];
    $userNumber=$_POST["userNumber"];
    $userState='Infected';
    $is_survey='no';

    $statement = mysqli_prepare($con, "DELETE FROM `USER` WHERE `USER`.`userID` = ?");
    mysqli_stmt_bind_param($statement, "s", $userID);
    mysqli_stmt_execute($statement);


    $response = array();
    $response["success"] = true;

    echo json_encode($response);

?>
