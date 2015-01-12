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
		@SuppressWarnings("unused")
		DifficultyReader values = new DifficultyReader(level.getXMLFile());
		/*System.out.println("Difficulty values: interval " + values.getIntervalDifficulty() +
				" range " + values.getRangeDifficulty());*/
		
		
		while (parse.hasMeasure()) {
			//System.out.println("Still more measures to read.");
			parse.parseMeasure();
		}
		
		parse.statusReport();
		
		
		//Apply level to parsed measures.
		
	}
}
