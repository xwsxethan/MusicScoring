package Nodes;

import org.w3c.dom.Node;

import Visitors.IMusicElementVisitor;

public class Part extends IVisitableXMLNode {

	public Part(Node elem) {
		base = elem;
	}
	
	public void accept (IMusicElementVisitor visitor) {
		visitor.visit(this);
	}

}
