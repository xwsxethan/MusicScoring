package DifficultyNodes;

import org.w3c.dom.Node;

import Visitors.*;

public abstract class IDifficultyXMLNode {
	protected Node base;
	
	public abstract void accept (IDifficultyElementVisitor visitor);
	
	public Node getBase() {
		return base;
	}
}
