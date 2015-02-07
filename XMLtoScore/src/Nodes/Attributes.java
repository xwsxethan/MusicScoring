package Nodes;

import org.w3c.dom.Node;

import Visitors.IMusicElementVisitor;

public class Attributes extends IVisitableXMLNode {

	public Attributes(Node elem) {
		base = elem;
	}
	
	public void accept(IMusicElementVisitor visitor) {
		visitor.visit(this);
	}

}
