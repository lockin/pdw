<?php

require 'rb.php';
R::setup('mysql:host=localhost;dbname=sonykuba_TIN','sonykuba_tin','tin2008');
R::freeze( true );

$token=str_replace(" ","",$_REQUEST['token']);
$startDate=date($_REQUEST['startDate']);
$stopDate=date($_REQUEST['stopDate']);
$doctorId=str_replace(" ","",$_REQUEST['doctorId']);

$pacient=R::findOne('Pacient','token = ? ',array($token));
if($pacient!=null){
	$doctor=R::findOne('Doctor','id= ? ',array($doctorId));
	if($doctor!=null){
		R::getAll( "SELECT * FROM PacientVisitNote  WHERE idPacientVisit = '".$visits[$i]["id"]."'");
	}else{
		print("FALSE");
	}
	$visitsNotes = R::getAll( "SELECT * FROM PacientVisitNote WHERE idDoctor = '".$doctor->id."' AND ((startDate >= '".$startDate."' AND startDate < '".$stopDate."') OR (stopDate > '".$startDate."' AND stopDate <= '".$stopDate."'))" );
	
	$length = count($visitsNotes);
	if($length>0){
		print("FALSE");
	}else{
		$pacientVisit=R::dispense( 'PacientVisit' );
		$pacientVisit->createDate=date_create()->format('Y-m-d H:i:s');
		$pacientVisit->idPacient=$pacient->id;
		R::store($pacientVisit);
		
		$pacientVisitNote=R::dispense( 'PacientVisitNote');
		$pacientVisitNote->idDoctor=$doctor->id;
		$pacientVisitNote->idPacientVisit=$pacientVisit->id;
		$pacientVisitNote->startDate=$startDate;
		$pacientVisitNote->stopDate=$stopDate;
		R::store($pacientVisitNote);
		print("TRUE");
	}	
}else{
	print("FALSE");
}

?>