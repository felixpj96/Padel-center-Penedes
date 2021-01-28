<?php

include "db.php";

$db= new DB();

if(isset($_POST['getNoticias']) && $_POST['getNoticias']=="true"){
    $result=$db->getNoticias();
  }


?>
