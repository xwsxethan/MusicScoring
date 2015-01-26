package Nodes;

import org.w3c.dom.Node;

import Visitors.IElementVisitor;

public class Attribute extends IVisitableXMLNode {

	public Attribute(Node elem) {
		base = elem;
	}
	
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
