package Nodes;

import Visitors.IElementVisitor;

public class Sound implements IVisitableXMLNode {

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}