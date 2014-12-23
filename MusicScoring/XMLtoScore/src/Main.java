import java.io.File;

import DifficultyLevels.*;


public class Main {
	private static final String FOLDER_NAME = System.getProperty("user.dir") + File.separator;
	private static final String FILE_NAME =
			"The_Hobbit_The_Desolation_of_Smaug_Medley_for_Solo_Clarinet.xml";
	private static final DifficultyLevel LEVEL = new Beginner();

	public static void main(String[] args) {
		String fullFileName = FOLDER_NAME + FILE_NAME;
		File xmlFile = new File(fullFileName);
		@SuppressWarnings("unused")
		Scorer letsGetSomeOutput = new Scorer(LEVEL, xmlFile);
		//Parser letsGetSomeOutput = new Parser(xmlFile);
		
	}

}
