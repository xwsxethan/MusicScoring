package Nodes;

import org.w3c.dom.Node;

import Visitors.IMusicElementVisitor;

public class DirectionType extends IVisitableXMLNode {
	
	public DirectionType(Node elem) {
		base = elem;
	}

	@Override
	public void accept(IMusicElementVisitor visitor) {
		visitor.visit(this);
	}

}
