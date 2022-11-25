package splat.lexer;

public class Token {

    private final String value;
    private final int column;
    private final int line;

    public String getValue() {
        return value;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }


    public Token(String value, int column, int line) {
        this.value = value;
        this.column = column;
        this.line = line;
    }

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value + '\'' +
                ", column=" + column +
                ", line=" + line +
                '}';
    }

}
