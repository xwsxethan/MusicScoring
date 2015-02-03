package Nodes;

import org.w3c.dom.Node;

import Visitors.IMusicElementVisitor;

public class BeatType extends IVisitableXMLNode {
	
	public BeatType(Node elem) {
		base = elem;
	}

	@Override
	public void accept(IMusicElementVisitor visitor) {
		visitor.visit(this);
	}

}
