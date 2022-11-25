package splat.parser.elements;

import splat.lexer.Token;

public class Type extends ASTElement{

    private final String type;

    public Type(Token tok, String type) {
        super(tok);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Type{" +
                "type='" + type + '\'' +
                '}';
    }
}
