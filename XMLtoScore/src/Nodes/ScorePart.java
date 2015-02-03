package Nodes;

import org.w3c.dom.Node;

import Visitors.IMusicElementVisitor;

public class ScorePart extends IVisitableXMLNode {
	
	public ScorePart(Node elem) {
		base = elem;
	}
	
	public void accept (IMusicElementVisitor visitor) {
		visitor.visit(this);
	}

}
