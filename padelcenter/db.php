<?php

class DB{
  private $dbHost="localhost";
  private $dbUsername="root";
  private $dbPassword="";
  private $dbName="padelcenter";

  public $db_con;

  public function __construct(){
    if(!isset($this->db)){
      try{
        $pdo=new PDO("mysql:host=".$this->dbHost.";dbname=".$this->dbName, $this->dbUsername, $this->dbPassword);
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $this->db_con=$pdo;
      }catch(PDOEXCEPTION $e){
        die("Failed to connect with mysql :".$e->getMessage());
      
      } 
    }
  }

  public function getName($email)
  {
    $stmt=$this->db_con->prepare("SELECT * FROM login_info WHERE email=:email");
    $stmt->execute(array(":email"=>$email));
    $row=$stmt->fetch(PDO::FETCH_ASSOC);
  
    return $row['nombre'];
  }

public function pistasReservadas($fecha, $pista)
  {
     $stmt=$this->db_con->prepare("SELECT fecha,pista FROM reserva_info WHERE fecha LIKE ? AND pista = ?"); 
     $stmt->bindValue(1,"%$fecha%",PDO::PARAM_STR);
     $stmt->bindValue(2,"$pista",PDO::PARAM_STR);
     $stmt->execute();

    $final = array();

    $results = $stmt->fetchAll(PDO::FETCH_ASSOC);
    echo json_encode($results);
  }
  public function getNoticias()
  {
     $stmt=$this->db_con->prepare("SELECT * FROM actualidad"); 
     $stmt->execute();

     $final = array();

     $results = $stmt->fetchAll(PDO::FETCH_ASSOC);
     echo json_encode($results);
  }
  public function getReservas()
  {
     $stmt=$this->db_con->prepare("SELECT * FROM reserva_info"); 
     $stmt->execute();

     $final = array();

     $results = $stmt->fetchAll(PDO::FETCH_ASSOC);
     echo json_encode($results);
  }
    
  
  /*
  public function pistasReservadas($fecha, $pista)
  {
    $stmt=$this->db_con->prepare("SELECT fecha,pista FROM reserva_info WHERE fecha LIKE :fecha"); 

    $stmt->execute(array(":fecha"=>$fecha));

    $final = array();

    $results = $stmt->fetchAll(PDO::FETCH_ASSOC);
    echo json_encode($results);
  }
*/
  public function userLogin($email, $password)
  {
    $stmt=$this->db_con->prepare("SELECT * FROM login_info WHERE email=:email");
    $stmt->execute(array(":email"=>$email));
    $row=$stmt->fetch(PDO::FETCH_ASSOC);
    $count=$stmt->rowCount();
    if($row['password']==$password){
      return $row['admin'];
    }else{
      return "error";
    }
  }

  public function registerUser($email, $password, $nombre)
  {
    $sql="INSERT into login_info(email, password, nombre) VALUES (:email, :password, :nombre)";
    $stmt=$this->db_con->prepare($sql);
    $stmt->bindParam(":email", $email);
    $stmt->bindParam(":password", $password);
    $stmt->bindParam(":nombre", $nombre);
    $stmt->execute();
    $newId=$this->db_con->lastInsertId();
    return $newId;
  }

  public function setReserva($fecha, $propietario, $pista)
  {
    $sql="INSERT into reserva_info(fecha, propietario, pista) VALUES (:fecha, :propietario, :pista)";
    $stmt=$this->db_con->prepare($sql);
    $stmt->bindParam(":fecha", $fecha);
    $stmt->bindParam(":propietario", $propietario);
    $stmt->bindParam(":pista", $pista);
    $stmt->execute();
    $newId=$this->db_con->lastInsertId();
    return $newId;
  }

   public function setNoticia($titulo, $texto)
  {
    $sql="INSERT into actualidad(titulo, texto) VALUES (:titulo, :texto)";
    $stmt=$this->db_con->prepare($sql);
    $stmt->bindParam(":titulo", $titulo);
    $stmt->bindParam(":texto", $texto);
    $stmt->execute();
    $newId=$this->db_con->lastInsertId();
    return $newId;
  }
  public function eliminarReserva($fecha, $pista)
  {
    $stmt=$this->db_con->prepare("DELETE FROM reserva_info WHERE fecha = ? AND pista = ?");
    $stmt->bindValue(1,"$fecha",PDO::PARAM_STR);
    $stmt->bindValue(2,"$pista",PDO::PARAM_STR);
    $stmt->execute();
  }
}
