<?php

include "db.php";

$db= new DB();

if(isset($_POST['loginuser']) && $_POST['loginuser']=="true"){
  if(!empty($_POST['email']) && !empty($_POST['password'])){
    $result=$db->userLogin($_POST['email'], $_POST['password']);
    if($result != "error"){
      echo $result;
    }else{
      echo "error";
    }
  }
}