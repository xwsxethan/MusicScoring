package Nodes;

import org.w3c.dom.Node;

import Visitors.*;

public abstract class IVisitableXMLNode {
	protected Node base;
	
	public abstract void accept (IMusicElementVisitor visitor);
	
	public Node getBase() {
		return base;
	}
}
