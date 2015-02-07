package Nodes;

import org.w3c.dom.Node;

import Visitors.IMusicElementVisitor;

public class Clef extends IVisitableXMLNode {

	public Clef(Node elem) {
		base = elem;
	}
	
	public void accept(IMusicElementVisitor visitor) {
		visitor.visit(this);
	}

}
