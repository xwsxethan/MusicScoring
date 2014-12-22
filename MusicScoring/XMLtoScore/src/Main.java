import java.io.File;


public class Main {
	private static final String FOLDER_NAME = "..//..//";
	private static final String FILE_NAME =
			"The_Hobbit_The_Desolation_of_Smaug_Medley_for_Solo_Clarinet.xml";
	private static final DifficultyLevel LEVEL = DifficultyLevel.Beginner;

	public static void main(String[] args) {
		String fullFileName = FOLDER_NAME + FILE_NAME;
		File xmlFile = new File(fullFileName);
		@SuppressWarnings("unused")
		Scorer letsGetSomeOutput = new Scorer(LEVEL, xmlFile);
		//Parser letsGetSomeOutput = new Parser(xmlFile);
		
	}

}
