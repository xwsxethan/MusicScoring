package Nodes;

import Visitors.IElementVisitor;

public class Pitch implements IVisitableXMLNode {

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
