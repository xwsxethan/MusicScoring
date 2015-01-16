package Nodes;

import java.util.List;

import org.w3c.dom.Node;

import Utilities.*;
import Visitors.IElementVisitor;

public class Score implements IVisitableXMLNode {
	
	private Node base;

	public Score(Node elem) {
		base = elem;
	}
	
	@Override
	public void accept(IElementVisitor visitor) {
		List<Node> elems = Utils.getElements(base, Constants.PART_NODE);
		
		for (Node elem : elems) {
			Part part = new Part(elem);
			visitor.visit(part);
		}
		
		visitor.visit(this);
	}

}
