package Visitors;

import Nodes.*;

public interface IElementVisitor {
	public void visit(Score score);
	
	public void visit(Part part);
	
	public void visit(Measure measure);
	
	public void visit(Note note);
	
	public void visit(Attribute attribute);
	
	public void visit(Direction direction);
	
	public void visit(DirectionType directionType);
	
	public void visit(Dynamics dynamics);
	
	public void visit(Key key);
	
	public void visit(Fifths fifths);
	
	public void visit(Time time);
	
	public void visit(BeatType beatType);
	
	public void visit(Sound sound);
	
	public void visit(Tempo tempo);
	
	public void visit(Pitch pitch);
	
	public void visit(Rest rest);
	
	public void visit(Type type);
	
	public void visit(Dotted dot);
	
	public void visit(Tied tie);
	
	public void visit(Start start);
	
	public void visit(Stop stop);
	
	public void visit(Step step);
	
	public void visit(Octave octave);
	
	public void visit(Alter alter);
	
}
