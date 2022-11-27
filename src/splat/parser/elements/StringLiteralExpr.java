package splat.parser.elements;

import splat.lexer.Token;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Expression;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class StringLiteralExpr extends Expression {

    private final String string_literal;

    public StringLiteralExpr(Token tok, String string_literal) {
        super(tok);
        this.string_literal = string_literal;
    }

    public String getString_literal() {
        return string_literal;
    }

    @Override
    public String toString() {
        return "StringLiteralExpr{" +
                "string_literal='" + string_literal + '\'' +
                '}';
    }


    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Token tok = new Token(getString_literal(),getColumn(),getLine());

        Type stringLiteralType = new Type(tok,"String");

        return stringLiteralType;
    }
}
