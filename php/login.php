<?php

require 'rb.php';
R::setup('mysql:host=localhost;dbname=sonykuba_TIN','sonykuba_tin','tin2008');
R::freeze( true );

$email=str_replace(" ","",$_REQUEST['email']);
$password=str_replace(" ","",$_REQUEST['password']);

$pacient=R::findOne('Pacient','email = ? AND passwordHash = ?',array($email, md5($password)));
if($pacient!=null){

	$date = date_create();
	$result = $date->format('Y-m-d H:i:s');
	$pacient->token=md5($result);
	R::store($pacient);
	print($pacient->token);
}else{
	print("FALSE");
}

?>