package Visitors;

import DifficultyNodes.*;

public interface IDifficultyElementVisitor {

	public void visit(NoteDifficulty noteDifficulty);

	public void visit(IndividualNoteDifficulty individualNoteDifficulty);

	public void visit(RangeDifficulty rangeDifficulty);
	
	public void visit(IntervalsDifficulty intervalDifficulty);
	
	public void visit(IndividualIntervalDifficulty individualIntervalDifficulty);
	
	public void visit(DifferenceDifficulty differenceDifficulty);
	
	public void visit(Location1Difficulty location1Difficulty);
	
	public void visit(Location2Difficulty location2Difficulty);
	
	public void visit(Difficulty Difficulty);
	
	public void visit(DynamicsDifficulty dynamicsDifficulty);
	
	public void visit(TempoDifficulty tempoDifficulty);
	
	public void visit(KeySignatureDifficulty keySignatureDifficulty);
	
	public void visit(IndividualKeySignatureDifficulty individualKeySignatureDifficulty);
	
	public void visit(KeyNameDifficulty keyNameDifficulty);
	
	public void visit(ArticulationDifficulty articulationDifficulty);
	
	public void visit(Levels levels);
}
