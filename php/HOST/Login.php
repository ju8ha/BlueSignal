<?php
    $con = mysqli_connect("localhost", "seatrea", "wldus1037!", "seatrea");
    mysqli_query($con,'SET NAMES utf8');

    $hostID = $_POST["hostID"];
    $hostPassword = $_POST["hostPassword"];

    $statement = mysqli_prepare($con, "SELECT * FROM HOST WHERE hostID = ? AND hostPassword = ?");
    mysqli_stmt_bind_param($statement, "ss", $hostID, $hostPassword);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $hostID, $hostPassword, $hostName, $hostNumber, $hostState);

    $response = array();
    $response["success"] = false;

    while(mysqli_stmt_fetch($statement)) {
        $response["success"] = true;
        $response["hostID"] = $hostID;
        $response["hostPassword"] = $hostPassword;
        $response["hostName"] = $hostName;
        $response["hostNumber"] = $hostNumber;
        $response["hostState"] = $hostState;
    }
    echo json_encode($response);

?>
