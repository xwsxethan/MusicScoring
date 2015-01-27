package DifficultyNodes;

import org.w3c.dom.Node;

import Visitors.IDifficultyElementVisitor;

public class NoteDifficulty extends IDifficultyXMLNode {
	
	public NoteDifficulty(Node elem) {
		base = elem;
	}

	@Override
	public void accept(IDifficultyElementVisitor visitor) {
		visitor.visit(this);

	}

}
