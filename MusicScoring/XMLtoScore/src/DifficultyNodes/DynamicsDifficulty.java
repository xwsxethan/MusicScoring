package DifficultyNodes;

import org.w3c.dom.Node;

import Visitors.IDifficultyElementVisitor;

public class DynamicsDifficulty extends IDifficultyXMLNode {
	
	public DynamicsDifficulty(Node elem) {
		base = elem;
	}

	@Override
	public void accept(IDifficultyElementVisitor visitor) {
		visitor.visit(this);

	}

}
