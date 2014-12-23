package DifficultyLevels;

public class Beginner extends DifficultyLevel {
	private static final String BEGINNER_FILE = "Beginner.xml";

	@Override
	public String getXMLFileLocation() {
		return CONFIG_FOLDER + BEGINNER_FILE;
	}
}
