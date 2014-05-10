<?php

require 'rb.php';
R::setup('mysql:host=localhost;dbname=sonykuba_TIN','sonykuba_tin','tin2008');
R::freeze( true );

$token=str_replace(" ","",$_REQUEST['token']);


$pacient=R::findOne('Pacient','token = ? ',array($token));
if($pacient!=null){

	$visits = R::getAll( "SELECT * FROM PacientVisit  WHERE idPacient = '".$pacient->id."'");
	$length = count($visits);
	for ($i = 0; $i < $length; $i++) {
		$visitNotes=R::getAll( "SELECT * FROM PacientVisitNote  WHERE idPacientVisit = '".$visits[$i]["id"]."'");
		
		$lengthVisitNotes = count($visitNotes);
		for ($l = 0; $l < $lengthVisitNotes ; $l++) {
			$doctors=R::getAll( "SELECT * FROM Doctor WHERE id = '".$visitNotes[$l]["idDoctor"]."'");	
			$visitNotes[$l]["Doctor"]=$doctors;
		}
		$visits[$i]["pacientVisitNote"]=$visitNotes;
	}
	
	print (json_encode($visits));
	
}else{
	print("FALSE");
}

?>