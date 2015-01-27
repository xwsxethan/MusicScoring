package Nodes;

import org.w3c.dom.Node;

import Visitors.IMusicElementVisitor;

public class Measure extends IVisitableXMLNode {

	public Measure(Node elem) {
		base = elem;
	}
	
	public void accept(IMusicElementVisitor visitor) {
		visitor.visit(this);
	}

}
