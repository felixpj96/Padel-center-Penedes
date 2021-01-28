<?php

include "db.php";

$db= new DB();

if(isset($_POST['getReservas']) && $_POST['getReservas']=="true"){
    $result=$db->getReservas();
}

?>