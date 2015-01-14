MusicScoring
======

This is a private repository for the research project I'm working on for my master's thesis. The code generates a difficulty/complexity measurement for a piece of music written in music xml. The paper will eventually be a software research paper to be submitted to conference.

In the current version, the default settings are to output a difficulty score for level1.xml (including in the repo) based on the beginner difficulty setting. To change the piece of music, specify the full path to an alternate xml file as a command line parameter. To change the difficulty setting, specify a number 1-5 inclusive representing the difficulty (1 is lowest, 5 is highest) as a command line parameter. Both of these can be changed simultaneously and can appear in any order as command line parameters.

The beginner difficulty setting is mostly hardcoded into the system, but will eventually be read in from an xml file. At the moment no other difficulty settings are in place, so changing the difficulty will do nothing. This feature will be implemented later, but the user functionality is already present. Changing the file should work however.
