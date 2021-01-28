<?php

include "db.php";

$db= new DB();

if(isset($_POST['reserva']) && $_POST['reserva']=="true"){
  if(!empty($_POST['fecha']) && !empty($_POST['propietario']) && !empty($_POST['pista'])){
    $result=$db->setReserva($_POST['fecha'], $_POST['propietario'], $_POST['pista']);
    if($result>0){
      echo "success";
    }else{
      echo $result;
    }
  }
}