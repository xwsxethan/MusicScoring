package Nodes;

import Visitors.IElementVisitor;

public class Time implements IVisitableXMLNode {

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
