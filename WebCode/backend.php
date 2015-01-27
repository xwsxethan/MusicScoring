<?php

// chdir("C:/Users/user/Dropbox/Public/College/2015-01 Spring/Thesis/MusicScoring/XMLtoScore");
$var = trim($_POST["xmlName"]);
if (empty($var)) {
	echo("No input file received. No score to post.");
}

echo shell_exec('java -jar MusicScoring.jar '.$var.' 1');
?>