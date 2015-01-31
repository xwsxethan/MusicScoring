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
		
		//parse.statusReport();
		
		visitor = new NoteComplexityVisitor(xml, aLevel);
		score = visitor.getFirstScore();
		
		/*
		//Portion for printing out all the scores gathered.
		for (int i = 0; i < visitor.getAmountOfScores(); i++) {
			System.out.println("Score Number " + i + " is: " + visitor.getScore(i));
		}*/
		
	}
	
	public double getSetScore() {
		return score;
	}
}
