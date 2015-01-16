package Nodes;

import Visitors.IElementVisitor;

public class Note implements IVisitableXMLNode {

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
