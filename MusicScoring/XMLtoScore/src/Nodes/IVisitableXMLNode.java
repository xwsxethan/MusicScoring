package Nodes;

import Visitors.*;

public interface IVisitableXMLNode {
	public void accept (IElementVisitor visitor);
}
