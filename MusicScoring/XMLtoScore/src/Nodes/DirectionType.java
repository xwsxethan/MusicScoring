package Nodes;

import org.w3c.dom.Node;

import Visitors.IElementVisitor;

public class DirectionType extends IVisitableXMLNode {
	
	public DirectionType(Node elem) {
		base = elem;
	}

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
