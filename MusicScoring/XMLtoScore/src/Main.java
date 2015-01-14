import java.io.File;

import DifficultyLevels.*;


public class Main {
	public static final boolean LOGGING = false;
	
	private static final String FOLDER_NAME = System.getProperty("user.dir") + File.separator;
	private static final String DEFAULT_FILE_NAME =
			//"The_Hobbit_The_Desolation_of_Smaug_Medley_for_Solo_Clarinet.xml";
			"level1.xml";
	private static final DifficultyLevel DEFAULT_LEVEL = new Beginner();
	private static final String xmlEnding = ".xml";

	public static void main(String[] args) {
		DifficultyLevel lev;
		String fullFileName;
		if (args.length == 0 || args[0] == null || args[0].isEmpty()) {
			fullFileName = FOLDER_NAME + DEFAULT_FILE_NAME;
			lev = DEFAULT_LEVEL;
		}
		else {
			int diffNum = 1;
			if (args[0].toLowerCase().endsWith(xmlEnding)) {
				fullFileName = FOLDER_NAME + args[0];
				if (args.length > 1 && args[1] != null && args[1].isEmpty()) {
					try {
						diffNum = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						diffNum = 1;
					}
				}
			}
			else {
				try {
					diffNum = Integer.parseInt(args[0]);
				} catch (NumberFormatException e) {
					diffNum = 1;
				}

				if (args.length > 1 && args[1] != null && args[1].isEmpty()) {
					fullFileName = FOLDER_NAME + args[1];					
				}
				else {
					fullFileName = FOLDER_NAME + DEFAULT_FILE_NAME;
				}
			}
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
		}
		
		File xmlFile = new File(fullFileName);
		@SuppressWarnings("unused")
		Scorer letsGetSomeOutput = new Scorer(lev, xmlFile);
		//Parser letsGetSomeOutput = new Parser(xmlFile);
		
	}

}
