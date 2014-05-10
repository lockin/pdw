<?php

require 'rb.php';
R::setup('mysql:host=localhost;dbname=sonykuba_TIN','sonykuba_tin','tin2008');
R::freeze( true );

$token=str_replace(" ","",$_REQUEST['token']);


$doctors=R::getAll( "SELECT * FROM Doctor");
print (json_encode($doctors));


?>