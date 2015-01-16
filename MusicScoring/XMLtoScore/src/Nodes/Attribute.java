package Nodes;

import Visitors.IElementVisitor;

public class Attribute implements IVisitableXMLNode {

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
