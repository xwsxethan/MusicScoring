package Nodes;

import Visitors.IElementVisitor;

public class BeatType implements IVisitableXMLNode {

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
