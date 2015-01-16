package Nodes;

import Visitors.IElementVisitor;

public class Type implements IVisitableXMLNode {

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
