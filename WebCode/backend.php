<?php

// chdir("C:/Users/user/Dropbox/Public/College/2015-01 Spring/Thesis/MusicScoring/XMLtoScore");
$fileName = trim($_POST["xmlName"]);
$difficulty = trim($_POST["difficulty"]);
if (empty($fileName)) {
	echo("No input file received. No score to post.");
}
if (empty($difficulty)) {
	$difficulty = '1';
}


echo shell_exec('java -jar MusicScoring.jar '.$fileName.' '.$difficulty);
?>