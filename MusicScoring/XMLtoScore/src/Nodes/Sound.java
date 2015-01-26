package Nodes;

import org.w3c.dom.Node;

import Visitors.IElementVisitor;

public class Sound extends IVisitableXMLNode {
	
	public Sound(Node elem) {
		base = elem;
	}

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visit(this);
	}

}
