package Nodes;

import org.w3c.dom.Node;

import Visitors.IMusicElementVisitor;

public class Attribute extends IVisitableXMLNode {

	public Attribute(Node elem) {
		base = elem;
	}
	
	public void accept(IMusicElementVisitor visitor) {
		visitor.visit(this);
	}

}
