package Nodes;

import Visitors.IElementVisitor;

public class Alter implements IVisitableXMLNode {

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
