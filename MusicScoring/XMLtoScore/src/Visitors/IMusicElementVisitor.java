package Visitors;

import Nodes.*;

public interface IMusicElementVisitor {
	public void visit(Score score);
	
	public void visit(PartList partList);
	
	public void visit(ScorePart scorePart);
	
	public void visit(PartName partName);
	
	public void visit(Part part);
	
	public void visit(Measure measure);
	
	public void visit(Note note);
	
	public void visit(Attribute attribute);
	
	public void visit(Direction direction);
	
	public void visit(DirectionType directionType);
	
	public void visit(DynamicsNode dynamics);
	
	public void visit(Key key);
	
	public void visit(Fifths fifths);
	
	public void visit(Time time);
	
	public void visit(BeatType beatType);
	
	public void visit(Sound sound);
	
	public void visit(Tempo tempo);
	
	public void visit(Pitch pitch);
	
	public void visit(Rest rest);
	
	public void visit(Notations notations);
	
}
