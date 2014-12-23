import java.io.File;

import DifficultyLevels.*;


public class Scorer {
	private File xml;
	private Parser parse;
	private DifficultyLevel level;
	
	public Scorer (DifficultyLevel aLevel, File xmlFile) {
		xml = xmlFile;
		parse = new Parser(xml);
		level = aLevel;
		
		//Read difficulty level file.
		DifficultyReader values = new DifficultyReader(level.getXMLFile());
		System.out.println("Difficulty values: interval " + values.getIntervalDifficulty() +
				" range " + values.getRangeDifficulty());
		
		
		while (parse.parseMeasures()) {
			System.out.println("Still more measures to read.");
		}		
		
		
		//Apply level to parsed measures.
	}
}
