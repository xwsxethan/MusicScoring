package Nodes;

import org.w3c.dom.Node;

import Visitors.IElementVisitor;

public class Measure extends IVisitableXMLNode {

	public Measure(Node elem) {
		base = elem;
	}
	
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
