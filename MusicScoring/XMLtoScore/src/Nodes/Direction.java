package Nodes;

import org.w3c.dom.Node;

import Visitors.IElementVisitor;

public class Direction extends IVisitableXMLNode {

	public Direction(Node elem) {
		base = elem;
	}
	
	public void accept (IElementVisitor visitor) {
		visitor.visit(this);
	}
}
