package Nodes;

import Visitors.IElementVisitor;

public class Dynamics implements IVisitableXMLNode {

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
