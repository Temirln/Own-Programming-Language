package splat.parser.elements.EXPR;

import splat.lexer.Token;
import splat.parser.elements.DECL.FunctionDecl;
import splat.parser.elements.Expression;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class BoolLiteralExpr extends Expression {

    private final String bool_literal;

    public BoolLiteralExpr(Token tok, String bool_literal) {
        super(tok);
        this.bool_literal = bool_literal;
    }

    public String getBool_literal() {
        return bool_literal;
    }

    @Override
    public String toString() {
        return "BoolLiteralExpr{" +
                "bool_literal='" + bool_literal + '\'' +
                '}';
    }


    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        return null;
    }
}
