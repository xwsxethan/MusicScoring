package Nodes;

import Visitors.IElementVisitor;

public class Fifths implements IVisitableXMLNode {

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
