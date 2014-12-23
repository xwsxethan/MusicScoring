package DifficultyLevels;

public class Novice extends DifficultyLevel {
	public static final String NOVICE_FILE = "Novice.xml";

	@Override
	public String getXMLFileLocation() {
		return CONFIG_FOLDER + NOVICE_FILE;
	}
	
}
