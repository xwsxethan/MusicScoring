package Visitors;

import Nodes.Alter;
import Nodes.Attribute;
import Nodes.BeatType;
import Nodes.Direction;
import Nodes.DirectionType;
import Nodes.Dotted;
import Nodes.Dynamics;
import Nodes.Fifths;
import Nodes.Key;
import Nodes.Measure;
import Nodes.Note;
import Nodes.Octave;
import Nodes.Part;
import Nodes.Pitch;
import Nodes.Rest;
import Nodes.Score;
import Nodes.Sound;
import Nodes.Start;
import Nodes.Step;
import Nodes.Stop;
import Nodes.Tempo;
import Nodes.Tied;
import Nodes.Time;
import Nodes.Type;

public class ParserElementVisitor implements IElementVisitor {

	@Override
	public void visit(Score score) {
		return;
	}

	@Override
	public void visit(Part part) {
		return;
	}

	@Override
	public void visit(Measure measure) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Note note) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Attribute attribute) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Direction direction) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(DirectionType directionType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Dynamics dynamics) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Key key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Fifths fifths) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Time time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(BeatType beatType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Sound sound) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Tempo tempo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Pitch pitch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Rest rest) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Type type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Dotted dot) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Tied tie) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Start start) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Stop stop) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Step step) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Octave octave) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Alter alter) {
		// TODO Auto-generated method stub

	}

}
