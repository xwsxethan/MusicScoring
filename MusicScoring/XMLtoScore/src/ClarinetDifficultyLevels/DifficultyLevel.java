package ClarinetDifficultyLevels;

import java.io.File;

public abstract class DifficultyLevel {
	public static final String CONFIG_FOLDER =
			System.getProperty("user.dir") + File.separator + "ConfigFiles" + File.separator;
	
	public abstract String getXMLFileLocation();
	
	public File getXMLFile() {
		return new File(getXMLFileLocation());
	}
}
