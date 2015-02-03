package Nodes;

import org.w3c.dom.Node;

import Visitors.IMusicElementVisitor;

public class PartName extends IVisitableXMLNode {
	private String name;

	public PartName(Node elem, String name) {
		base = elem;
		this.name = name;
	}
	
	public void accept (IMusicElementVisitor visitor) {
		visitor.visit(this);
	}
	
	public String getName() {
		return name;
	}

}
