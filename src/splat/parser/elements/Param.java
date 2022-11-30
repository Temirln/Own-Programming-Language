package splat.parser.elements;

import splat.lexer.Token;

public class Param extends ASTElement{

    private final String label;
    private final Type type;

    public Param(Token tok, String label, Type type) {
        super(tok);
        this.label = label;
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Param{" +
                "label='" + label + '\'' +
                ", type=" + type +
                '}';
    }
}
