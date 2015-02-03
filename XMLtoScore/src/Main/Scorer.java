package Main;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;

import Visitors.NoteComplexityVisitor;
import DifficultyLevels.DifficultyLevel;


public class Scorer {
	private File xml;
	//private ParserStarter parse;
	//private DifficultyLevel level;
	private NoteComplexityVisitor visitor;
	private double score;
	private List<ComplexityScore> allScoredParts;
	private String json;
	
	@SuppressWarnings("unchecked")
	public Scorer (DifficultyLevel aLevel, File xmlFile) {
		xml = xmlFile;
		
		//parse.statusReport();
		
		visitor = new NoteComplexityVisitor(xml, aLevel);
		allScoredParts = visitor.getAllScores();
		try {
			score = allScoredParts.get(0).getOverallScore();
		}
		catch (IndexOutOfBoundsException e) {
			score = 0.0;
		}
		
		/*
		//Portion for printing out all the scores gathered.
		for (int i = 0; i < visitor.getAmountOfScores(); i++) {
			System.out.println("Score Number " + i + " is: " + visitor.getScore(i));
		}*/
		
		/*if (Main.LOGGING) {
			visitor.statusReport();
		}*/

		@SuppressWarnings("rawtypes")
		List  l1 = new LinkedList();
		
		for (ComplexityScore score : allScoredParts) {
			@SuppressWarnings("rawtypes")
			Map m1 = new LinkedHashMap();
			m1.put("partName", score.getName());
			m1.put("overallScore", score.getOverallScore());
			m1.put("otherFieldsSet", score.getOtherFieldsSet());
			m1.put("worstMeasureNumber", score.getMostDifficultMeasureNumber());
			m1.put("worstMeasureValue", score.getMostDifficultMeasureValue());
			m1.put("noteTotal", score.getTotalNoteScore());
			m1.put("intervalTotal", score.getTotalIntervalScore());
			l1.add(m1);
		}

		json = JSONValue.toJSONString(l1);
		                
		//System.out.println(jsonString);
		
	}
	
	public double getSetScore() {
		return score;
	}
	
	public String getJsonString() {
		return json;
	}
}
