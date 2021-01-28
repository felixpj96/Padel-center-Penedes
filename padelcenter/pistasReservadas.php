<?php

include "db.php";

$db= new DB();

if(isset($_POST['pistasReservadas']) && $_POST['pistasReservadas']=="true"){
  if(!empty($_POST['fecha']) && !empty($_POST['pista'])){
    $result=$db->pistasReservadas($_POST['fecha'], $_POST['pista']);
  }
}

?>
