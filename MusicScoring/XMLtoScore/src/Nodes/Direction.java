package Nodes;

import Visitors.IElementVisitor;

public class Direction implements IVisitableXMLNode {

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
