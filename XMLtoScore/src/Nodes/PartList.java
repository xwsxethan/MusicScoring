package Nodes;

import org.w3c.dom.Node;

import Visitors.IMusicElementVisitor;

public class PartList extends IVisitableXMLNode {

	public PartList(Node elem) {
		base = elem;
	}
	
	public void accept (IMusicElementVisitor visitor) {
		visitor.visit(this);
	}

}
