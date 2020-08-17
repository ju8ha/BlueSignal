<?php
    $con = mysqli_connect("localhost", "seatrea", "wldus1037!", "seatrea");

    $hostID = $_POST["hostID"];

    $statement = mysqli_prepare($con, "SELECT hostID FROM HOST WHERE hostID = ?");
    mysqli_stmt_bind_param($statement, "s", $hostID);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $hostID);

    $response = array();
    $response["success"] = true;

    while(mysqli_stmt_fetch($statement)){
        $response["success"]=false;
        $response["hostID"]=$hostID;
    }

    echo json_encode($response);

?>
