<?php

include "db.php";

$db= new DB();

if(isset($_POST['new']) && $_POST['new']=="true"){
  if(!empty($_POST['titulo']) && !empty($_POST['texto'])){
    $result=$db->setNoticia($_POST['titulo'], $_POST['texto']);
    if($result>0){
      echo "success";
    }else{
      echo $result;
    }
  }
}