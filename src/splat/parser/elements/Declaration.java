package splat.parser.elements;

import splat.lexer.Token;

public abstract class Declaration extends ASTElement {

	private final String label;

	public Declaration(Token tok, String label) {
		super(tok);
		this.label = label;
	}

	public String getLabel() {
		return label;
	}


}
