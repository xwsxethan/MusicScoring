package Nodes;

import org.w3c.dom.Node;

import Visitors.IMusicElementVisitor;

public class Score extends IVisitableXMLNode {
	
	public Score(Node elem) {
		base = elem;
	}
	
	public void accept (IMusicElementVisitor visitor) {
		visitor.visit(this);
	}

}
