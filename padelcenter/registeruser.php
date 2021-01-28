<?php

include "db.php";

$db= new DB();

if(isset($_POST['registeruser']) && $_POST['registeruser']=="true"){
  if(!empty($_POST['email']) && !empty($_POST['password']) && !empty($_POST['nombre'])){
    $result=$db->registerUser($_POST['email'], $_POST['password'], $_POST['nombre']);
    if($result>0){
      echo "success";
    }else{
      echo "error";
    }
  }
}