package Nodes;

import Visitors.IElementVisitor;

public class Start implements IVisitableXMLNode {

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
