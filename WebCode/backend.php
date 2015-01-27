<?php
// Relative version should be ../MusicScoring/XMLtoScore/
// chdir("C:/Users/user/Dropbox/Public/College/2015-01 Spring/Thesis/MusicScoring/XMLtoScore");

// Relative jar is ../Executables/MusicScoring.jar
// Relative xml is The_Hobbit_The_Desolation_of_Smaug_Medley_for_Solo_Clarinet.xml
echo shell_exec('java -jar MusicScoring.jar "The_Hobbit_The_Desolation_of_Smaug_Medley_for_Solo_Clarinet.xml" 1');
?>