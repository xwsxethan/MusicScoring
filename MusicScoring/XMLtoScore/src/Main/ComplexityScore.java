package Main;

import Utilities.Utils;

public class ComplexityScore {
	private double overallScore;
	private String name;
	
	private int totalMeasures;
	private int totalNotes;
	private double totalNoteDuration;
	private int totalInterval;
	private int totalNoteChanges;
	private int totalKeyChanges;
	private int totalBeatsPerMeasureChanges;
	private int totalTempoChanges;
	private int totalDynamicChanges;
	private int totalArticulationChanges;
	
	private int highNote;
	private int lowNote;
	private double highDuration;
	private double lowDuration;
	private int highInterval;
	private int lowInterval;
	
	private int worstMeasureNumber;
	private double worstMeasureValue;
	
	private boolean otherFieldsSet;
	
	public ComplexityScore(double totalScoreValue, String name) {
		overallScore = totalScoreValue;
		this.name = name;
		otherFieldsSet = false;
	}
	
	public ComplexityScore(double totalScoreValue, String name, int measureAmount, int noteAmount,
			double durationAmount, int intervalAmount, int noteChanges, int keyChanges,
			int bpmChanges, int tempoChanges, int dynamicChanges, int articulationChanges,
			int highNote, int lowNote, double highDuration, double lowDuration, int highInterval,
			int lowInterval, int worstMeasureNumber, double worstMeasureValue) {
		
		overallScore = totalScoreValue;
		this.name = name;
		
		totalMeasures = measureAmount;
		totalNotes = noteAmount;
		totalNoteDuration = durationAmount;
		totalInterval = intervalAmount;
		totalNoteChanges = noteChanges;
		totalKeyChanges = keyChanges;
		totalBeatsPerMeasureChanges = bpmChanges;
		totalTempoChanges = tempoChanges;
		totalDynamicChanges = dynamicChanges;
		totalArticulationChanges = articulationChanges;

		this.highNote = highNote;
		this.lowNote = lowNote;
		this.highDuration = highDuration;
		this.lowDuration = lowDuration;
		this.highInterval = highInterval;
		this.lowInterval = lowInterval;
		
		this.worstMeasureNumber = worstMeasureNumber;
		this.worstMeasureValue = worstMeasureValue;
		
		otherFieldsSet = true;
	}
	
	public double getOverallScore() {
		return overallScore;
	}
	
	public String getName() {
		return name;
	}
	
	public int getMostDifficultMeasureNumber() {
		return worstMeasureNumber;
	}
	
	public double getMostDifficultyMeasureValue() {
		return worstMeasureValue;
	}
	
	public void statusReport() {
		
		System.out.println("Overall score: " + overallScore);
		System.out.println("Part name: " + name);
		if (otherFieldsSet) {
			System.out.println("Total measures: " + totalMeasures);
			System.out.println("Total notes: " + totalNotes);
			System.out.println("Range: " + (highNote - lowNote) + " chromatic steps");
			System.out.println("Average Note Duration: " + (totalNoteDuration / totalNotes) + " seconds (I think)");
			System.out.println("Average Interval: " + (totalInterval / (totalNotes - 1)) + " chromatic steps");
			System.out.println("Total altered notes (key or accidental): " + totalNoteChanges);
			System.out.println("Total key changes: " + totalKeyChanges);
			System.out.println("Total beats per measure changes: " + totalBeatsPerMeasureChanges);
			System.out.println("Total tempo changes: " + totalTempoChanges);
			System.out.println("Total dynamic changes: " + totalDynamicChanges);
			System.out.println("Total articulation changes: " + totalArticulationChanges);
			System.out.println("Most difficult measure: #" + worstMeasureNumber + " with score " + worstMeasureValue);
			if (Main.LOGGING) {
				//System.out.println("Total objects: " + measureCount);
				System.out.println("High Note: " + highNote + " or " + Utils.numToNote(highNote));
				System.out.println("Low Note: " + lowNote + " or " + Utils.numToNote(lowNote));
				System.out.println("Total Note Duration: " + totalNoteDuration + " seconds (I think)");
				System.out.println("High Duration: " + highDuration + " seconds (I think)");
				System.out.println("Low Duration: " + lowDuration + " seconds (I think)");
				System.out.println("Total Interval: " + totalInterval + " chromatic steps");
				System.out.println("High Interval: " + highInterval + " chromatic steps");
				System.out.println("Low Interval: " + lowInterval + " chromatic steps");
				//System.out.println("Current Key: " + currentKey);
				//System.out.println("Current beats per measure: " + beatsPerMeasure);
				//System.out.println("Current tempo: " + tempo);
				//System.out.println("Current dynamics: " + dynamics.name());
				//System.out.println("Current articulations: " + articulations.name());
			}
		}
	
	}
}
