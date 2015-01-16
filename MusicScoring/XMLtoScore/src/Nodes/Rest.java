package Nodes;

import Visitors.IElementVisitor;

public class Rest implements IVisitableXMLNode {

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
