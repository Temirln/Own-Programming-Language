package splat.parser.elements;

import splat.lexer.Token;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Expression;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class IntLiteralExpr extends Expression {

    @Override
    public String toString() {
        return "IntLiteralExpr{" +
                "int_literal='" + int_literal + '\'' +
                '}';
    }

    public String getInt_literal() {
        return int_literal;
    }

    private final String int_literal;

    public IntLiteralExpr(Token tok, String int_literal) {
        super(tok);
        this.int_literal = int_literal;
    }


    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {

        Token tok = new Token(getInt_literal(),getColumn(),getLine());
        Type type = new Type(tok,"Integer");
        return type;
    }
}