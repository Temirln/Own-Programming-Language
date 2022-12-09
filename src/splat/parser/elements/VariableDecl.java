package splat.parser.elements;

import splat.lexer.Token;
import splat.parser.elements.Declaration;
import splat.parser.elements.Type;

public class VariableDecl extends Declaration {

	// Need to add some fields
//	private final String label;
	private final Type type;


	// Need to add extra arguments for setting fields in the constructor
	public VariableDecl(Token tok, String label, Type type) {
		super(tok, label);
//		this.label = label;
		this.type = type;
	}

//	public String getLabel() {
//		return label;
//	}

	public Type getType() {
		return type;
	}
// Getters?


	@Override
	public String toString() {
		return "VariableDecl{" +
				"label='" + getLabel() + '\'' +
				", type='" + type + '\'' +
				'}';
	}
}
