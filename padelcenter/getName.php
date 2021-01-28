<?php

include "db.php";

$db= new DB();

if(isset($_POST['loginuser']) && $_POST['loginuser']=="true"){
  if(!empty($_POST['email']) && !empty($_POST['email'])){
    $result=$db->getName($_POST['email']);
     if (!empty($result)) {
      echo $result;
    }else{
      echo "error";
    }
  }
}

?>
