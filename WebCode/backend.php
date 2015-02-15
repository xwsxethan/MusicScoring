<?php

// chdir("C:/Users/user/Dropbox/Public/College/2015-01 Spring/Thesis/MusicScoring/XMLtoScore");
$fileName = trim($_POST["xmlName"]);
$difficulty = trim($_POST["difficulty"]);
$validation = (trim($_POST["validation"]) === 'true');
if (empty($fileName)) {
	echo("No input file received. No score to post.");
}
if (empty($difficulty)) {
	$difficulty = '1';
}

$extraParams = "";
if ($validation) {
	$extraParams = $extraParams.' --validation=true';
}


echo shell_exec('java -jar MusicScoring.jar '.$fileName.' '.$difficulty.$extraParams);
?>