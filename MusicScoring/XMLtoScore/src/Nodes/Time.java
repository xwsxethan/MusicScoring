package Nodes;

import org.w3c.dom.Node;

import Visitors.IElementVisitor;

public class Time extends IVisitableXMLNode {
	
	public Time(Node elem) {
		base = elem;
	}

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
