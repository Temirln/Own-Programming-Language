package splat.parser.elements;

import splat.lexer.Token;
import splat.parser.elements.FunctionDecl;
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

        Type expr1Type = getExpr1().analyzeAndGetType(funcMap,varAndParamMap);
        Type expr2Type = getExpr2().analyzeAndGetType(funcMap,varAndParamMap);

        if (!expr1Type.getType().equals(expr2Type.getType())){
            throw new SemanticAnalysisException("Arguments Types must match",getExpr1());
        }

        Token tok = new Token(getBinaryOp(),getExpr1().getColumn(),getExpr1().getLine());
        Type boolType = new Type(tok,"Boolean");

        switch (getBinaryOp()){
            case "and":
            case "or":
                if (expr1Type.getType().equals("Boolean")){
                    return boolType;
                }
                else {
                    throw new SemanticAnalysisException("Types of the Expressions should be Boolean",getExpr1());
                }
            case "%":
            case "-":
            case "/":
            case "*":
                if (expr1Type.getType().equals("Integer")){
                    return expr1Type;
                }
                else {
                    throw new SemanticAnalysisException("Types of the Expressions should be Integer",getExpr1());
                }
            case ">":
            case "<":
            case ">=":
            case "<=":
                if (expr1Type.getType().equals("Integer")){
                    return boolType;
                }else {
                    throw new SemanticAnalysisException("Types of the Expressions should be Integer",getExpr1());
                }
            case "==":
                return boolType;
            case "+":
                if (!expr1Type.getType().equals("Boolean")){
                    return expr1Type;
                }
                else{
                    throw new SemanticAnalysisException("Types of the Expressions shouldn't be Boolean",getExpr1());
                }
            default:
                throw new SemanticAnalysisException("Invalid Binary Operations",getExpr1());
        }
    }
}