package Main;
import java.io.File;

import Visitors.NoteComplexityVisitor;
import DifficultyLevels.DifficultyLevel;


public class Scorer {
	private File xml;
	//private ParserStarter parse;
	//private DifficultyLevel level;
	private NoteComplexityVisitor visitor;
	private double score;
	
	public Scorer (DifficultyLevel aLevel, File xmlFile) {
		xml = xmlFile;
		//parse = new ParserStarter(xml, aLevel);
		//level = aLevel;
		
		/*System.out.println("Difficulty values: interval " + values.getIntervalDifficulty() +
				" range " + values.getRangeDifficulty());*/
		
		
		/*while (parse.hasMeasure()) {
			//System.out.println("Still more measures to read.");
			parse.parseMeasure();
		}*/
		
		//parse.statusReport();
		
		visitor = new NoteComplexityVisitor(xml, aLevel);
		score = visitor.getScore();
		
		//System.out.println("Current score for the first part: " + score);
		
		
		//Apply level to parsed measures.
		
	}
	
	public double getSetScore() {
		return score;
	}
}
