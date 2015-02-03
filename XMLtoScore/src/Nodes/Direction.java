package Nodes;

import org.w3c.dom.Node;

import Visitors.IMusicElementVisitor;

public class Direction extends IVisitableXMLNode {

	public Direction(Node elem) {
		base = elem;
	}
	
	public void accept (IMusicElementVisitor visitor) {
		visitor.visit(this);
	}
}
