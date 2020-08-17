<?php
    $con = mysqli_connect("localhost", "seatrea", "wldus1037!", "seatrea");
    mysqli_query($con,'SET NAMES utf8');

    $hostID = $_POST["hostID"];
    $hostPassword = $_POST["hostPassword"];
    $hostName = $_POST["hostName"];
    $hostNumber=$_POST["hostNumber"];
    $hostState='Infected';

    $statement = mysqli_prepare($con, "INSERT INTO HOST VALUES (?,?,?,?,?)");
    mysqli_stmt_bind_param($statement, "sssss", $hostID, $hostPassword, $hostName, $hostNumber, $hostState);
    mysqli_stmt_execute($statement);


    $response = array();
    $response["success"] = true;


    echo json_encode($response);

?>
