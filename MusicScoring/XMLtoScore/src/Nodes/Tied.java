package Nodes;

import Visitors.IElementVisitor;

public class Tied implements IVisitableXMLNode {

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
