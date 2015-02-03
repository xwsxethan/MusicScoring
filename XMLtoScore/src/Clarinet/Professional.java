package Clarinet;

import DifficultyLevels.DifficultyLevel;

public class Professional extends DifficultyLevel {
	public static final String PROFESSIONAL_FILE = "Professional.xml";

	@Override
	public String getXMLFileLocation() {
		return CONFIG_FOLDER + PROFESSIONAL_FILE;
	}

}
