<?php
    $con = mysqli_connect("localhost", "seatrea", "wldus1037!", "seatrea");
    mysqli_query($con,'SET NAMES utf8');

    $guest_id = $_POST["guest_id"];
    $host_id = $_POST["host_id"];
    $time1 = $_POST["time1"];
    $date1 = $_POST["date1"];

    $statement = mysqli_prepare($con, "INSERT INTO TIME1 VALUES (?,?,?,?)");
    mysqli_stmt_bind_param($statement, "ssss", $guest_id, $host_id, $time1, $date1);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);

?>
