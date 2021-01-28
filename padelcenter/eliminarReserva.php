<?php

include "db.php";

$db= new DB();

if(isset($_POST['eliminarReserva']) && $_POST['eliminarReserva']=="true"){
  if(!empty($_POST['fecha']) && !empty($_POST['pista'])){
    $result=$db->eliminarReserva($_POST['fecha'], $_POST['pista']);
  }
}