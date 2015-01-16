package Nodes;

import java.util.List;

import org.w3c.dom.Node;

import Utilities.Constants;
import Utilities.Utils;
import Visitors.IElementVisitor;

public class Part implements IVisitableXMLNode {
	
	private Node base;
	private List<Measure> measures;

	public Part(Node elem) {
		base = elem;
	}
	
	@Override
	public void accept(IElementVisitor visitor) {
		List<Node> elems = Utils.getElements(base, Constants.PART_NODE);
		
		for (Node elem : elems) {
			Measure measure = new Measure(elem);
			measures.add(measure);
			visitor.visit(measure);
		}
		
		visitor.visit(this);
	}

}
