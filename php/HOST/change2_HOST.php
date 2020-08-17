
<?php
    $con = mysqli_connect("localhost", "seatrea", "wldus1037!", "seatrea");
    mysqli_query($con,'SET NAMES utf8');


    $hostID = $_POST["hostID"];
    $hostPassword = $_POST["hostPassword"];
    $hostName = $_POST["hostName"];
    $hostNumber=$_POST["hostNumber"];

    $statement = mysqli_prepare($con, "UPDATE `HOST` SET `hostPassword` = ? WHERE `HOST`.`hostID` = ?");
    mysqli_stmt_bind_param($statement, "ss", $hostPassword, $hostID);
    mysqli_stmt_execute($statement);


    $response = array();
    $response["success"] = true;

    echo json_encode($response);

?>
