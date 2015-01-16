package Nodes;

import Visitors.IElementVisitor;

public class DirectionType implements IVisitableXMLNode {

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
