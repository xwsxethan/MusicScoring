package ClarinetDifficultyLevels;

public class Advanced extends DifficultyLevel {
	public static final String ADVANCED_FILE = "Advanced.xml";

	@Override
	public String getXMLFileLocation() {
		return CONFIG_FOLDER + ADVANCED_FILE;
	}

}
