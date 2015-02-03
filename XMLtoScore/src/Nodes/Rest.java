package Nodes;

import org.w3c.dom.Node;

import Visitors.IMusicElementVisitor;

public class Rest extends IVisitableXMLNode {
	
	public Rest(Node elem) {
		base = elem;
	}

	@Override
	public void accept(IMusicElementVisitor visitor) {
		visitor.visit(this);
	}

}
