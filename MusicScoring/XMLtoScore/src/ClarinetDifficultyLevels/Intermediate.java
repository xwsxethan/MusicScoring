package ClarinetDifficultyLevels;

public class Intermediate extends DifficultyLevel {
	public static final String INTERMEDIATE_FILE = "Intermediate.xml";

	@Override
	public String getXMLFileLocation() {
		return CONFIG_FOLDER + INTERMEDIATE_FILE;
	}

}
