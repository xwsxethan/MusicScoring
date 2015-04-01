package Main;
import java.io.File;

import org.json.simple.JSONObject;

import Clarinet.*;
import DifficultyLevels.DifficultyLevel;


public class Main {
	public static boolean LOGGING = false;
	private static boolean validation;
	private static boolean lots = false;
	
	/*private static final String FOLDER_NAME = System.getProperty("user.dir") + File.separator;
	private static final String DEFAULT_FILE_NAME =
			//"The_Hobbit_The_Desolation_of_Smaug_Medley_for_Solo_Clarinet.xml";
			//"level1.xml";
			"level2.xml";*/
	private static final DifficultyLevel DEFAULT_LEVEL = new Beginner();
	private static final String xmlEnding = ".xml";
	private static final String TURN_ON_LOGGING = "--logging=true";
	private static final String TURN_ON_VALIDATION = "--validation=true";
	
	private static String fullFileName;
	private static int diffNum;

	public static void main(String[] args) {
		validation = false;
		diffNum = 1;
		if (args.length == 0 || args[0] == null || args[0].isEmpty()) {
			outputError();
			return;
		}
		else {
			for (int i = 0; i < args.length; i++) {
				attemptSetXml(args[i]);
				attemptSetDifficulty(args[i]);
				attemptSetLogging(args[i]);
				attemptSetValidation(args[i]);
			}
		}
		
		DifficultyLevel lev = setDifficultyFromNum();
		File xmlFile;
		
		if (lots) {
			runLotsOfFilesAtOnce(lev);
			return;
		}
		
		try {
			xmlFile = new File(fullFileName);
		}
		catch (NullPointerException e) {
			outputError();
			return;
		}
		
		//Can no longer enable logging via command line since we're passing around json.
		LOGGING = false;
		
		if (LOGGING) {
			System.out.println("Logging enabled via command line. Now beginning parsing process.");
		}
		
		Scorer letsGetSomeOutput = new Scorer(lev, xmlFile, validation);

		System.out.println(letsGetSomeOutput.getJsonString());
		
		return;
		
	}
	
	@SuppressWarnings("unchecked")
	private static void outputError() {
		JSONObject obj = new JSONObject();
	    obj.put("errorMessage", "ERROR: No input file specified. Now quitting.");
		System.out.println(obj.toString());
	}

	private static void attemptSetXml(String arg) {
		if (arg != null && !arg.isEmpty() && arg.toLowerCase().endsWith(xmlEnding) && fullFileName == null) {
			fullFileName = arg;
		}
	}
	
	private static void attemptSetDifficulty(String arg) {
		if (arg != null && !arg.isEmpty()) {
			try {
				diffNum = Integer.parseInt(arg);
			} catch (NumberFormatException e) {
				diffNum = 1;
			}
		}
	}

	private static void attemptSetLogging(String arg) {
		if (arg != null && !arg.isEmpty() && arg.equalsIgnoreCase(TURN_ON_LOGGING)) {
			LOGGING = true;
		}
	}

	private static void attemptSetValidation(String arg) {
		if (arg != null && !arg.isEmpty() && arg.equalsIgnoreCase(TURN_ON_VALIDATION)) {
			validation = true;
		}
	}

	private static DifficultyLevel setDifficultyFromNum() {
		DifficultyLevel lev;
		switch (diffNum) {
		case (1) :
			lev = new Beginner();
			break;
		case (2) :
			lev = new Novice();
			break;
		case (3) :
			lev = new Intermediate();
			break;
		case (4) :
			lev = new Advanced();
			break;
		case (5) :
			lev = new Professional();
			break;
		default :
			if (diffNum < 1) { lev = new Beginner(); }
			else if (diffNum > 5) { lev = new Professional(); }
			else { lev = DEFAULT_LEVEL; }
			break;
		}
		
		return lev;
	}
	
	private static void runLotsOfFilesAtOnce(DifficultyLevel lev) {
		//Put in the directory for all the files you need to run at once
		String dirLocation = "/Users/ethanholder/Dropbox/Public/college/2015-01 Spring/OS X Thesis/MusicScoring/ClarinetSpecificSamplePieces/Graded Pieces/MusicXMLs to Run/";
		File dir = new File(dirLocation);
		
		File[] lotsOfFiles = dir.listFiles();
		
		for (int i = 0; i < lotsOfFiles.length; i++) {	
			File xmlFile = lotsOfFiles[i];
			
			if (xmlFile.getAbsolutePath().contains("Charles") || xmlFile.getAbsolutePath().contains("Scherzo")) {
				System.out.print("");
			}
			
			//Can no longer enable logging via command line since we're passing around json.
			LOGGING = false;
			
			if (LOGGING) {
				System.out.println("Logging enabled via command line. Now beginning parsing process.");
			}
			
			Scorer letsGetSomeOutput = new Scorer(lev, xmlFile, validation);
	
			System.out.println("File: " + xmlFile);
			System.out.println("Score: " + letsGetSomeOutput.getSetScore());
		}
	}
}
