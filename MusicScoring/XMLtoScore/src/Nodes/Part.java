package Nodes;

import org.w3c.dom.Node;

import Visitors.IElementVisitor;

public class Part extends IVisitableXMLNode {

	public Part(Node elem) {
		base = elem;
	}
	
	public void accept (IElementVisitor visitor) {
		visitor.visit(this);
	}

}
