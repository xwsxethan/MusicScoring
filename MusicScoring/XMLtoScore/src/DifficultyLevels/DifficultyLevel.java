package DifficultyLevels;

import java.io.File;

public abstract class DifficultyLevel {
	public static final String CONFIG_FOLDER =  
			"ConfigFiles" + File.separator;
			///XMLtoScore/ConfigFiles/Beginner.xml
	
	public abstract String getXMLFileLocation();
	
	public File getXMLFile() {
		return new File(getXMLFileLocation());
	}
}
