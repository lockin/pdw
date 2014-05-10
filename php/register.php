<?php

require 'rb.php';
R::setup('mysql:host=localhost;dbname=sonykuba_TIN','sonykuba_tin','tin2008');
R::freeze( true );

$email=str_replace(" ","",$_REQUEST['email']);
$firstName=str_replace(" ","",$_REQUEST['firstName']);
$lastName=str_replace(" ","",$_REQUEST['lastName']);
$password=str_replace(" ","",$_REQUEST['password']);

$pacient=R::findOne('Pacient','email = ?',array($email));
if($pacient==null){

	$pacient=R::dispense( 'Pacient' );
	$pacient->firstName=$firstName;
	$pacient->lastName=$lastName;
	$pacient->email=$email;
	$pacient->passwordHash=md5($password);
	R::store($pacient);
	print( json_encode(array("success"=>true)));
}else{
	print( json_encode(array("success"=>false)));
}

?>