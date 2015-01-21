package Nodes;

import org.w3c.dom.Node;

import Visitors.IElementVisitor;

public class Measure implements IVisitableXMLNode {

	@SuppressWarnings("unused")
	private Node base;

	public Measure(Node elem) {
		base = elem;
	}
	
	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
