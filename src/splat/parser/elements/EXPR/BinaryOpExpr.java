package splat.parser.elements.EXPR;

import splat.lexer.Token;
import splat.parser.elements.DECL.FunctionDecl;
import splat.parser.elements.Expression;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class BinaryOpExpr extends Expression {
    private final Expression expr1;
    private final Expression expr2;
    private final String binaryOp;

    public BinaryOpExpr(Token tok, Expression expr1, Expression expr2, String binaryOp) {
        super(tok);
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.binaryOp = binaryOp;
    }

    public Expression getExpr1() {
        return expr1;
    }

    public Expression getExpr2() {
        return expr2;
    }

    public String getBinaryOp() {
        return binaryOp;
    }

    @Override
    public String toString() {
        return "BinaryOpExpr{" +
                "expr1=" + expr1 +
                ", expr2=" + expr2 +
                ", binaryOp='" + binaryOp + '\'' +
                '}';
    }


    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        return null;
    }
}
