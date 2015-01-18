package Main;
import java.io.File;

import DifficultyLevels.DifficultyLevel;


public class Scorer {
	private File xml;
	private ParserStarter parse;
	//private DifficultyLevel level;
	
	public Scorer (DifficultyLevel aLevel, File xmlFile) {
		xml = xmlFile;
		parse = new ParserStarter(xml, aLevel);
		//level = aLevel;
		
		/*System.out.println("Difficulty values: interval " + values.getIntervalDifficulty() +
				" range " + values.getRangeDifficulty());*/
		
		
		while (parse.hasMeasure()) {
			//System.out.println("Still more measures to read.");
			parse.parseMeasure();
		}
		
		//parse.statusReport();
		
		System.out.println("Current score: " + parse.getScore());
		
		
		//Apply level to parsed measures.
		
	}
}
