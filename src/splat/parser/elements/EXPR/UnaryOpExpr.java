package splat.parser.elements.EXPR;

import splat.lexer.Token;
import splat.parser.elements.DECL.FunctionDecl;
import splat.parser.elements.Expression;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class UnaryOpExpr extends Expression {

    private final String unaryOp;
    private final Expression expr;

    public UnaryOpExpr(Token tok, String unaryOp, Expression expr) {
        super(tok);
        this.unaryOp = unaryOp;
        this.expr = expr;
    }

    public String getUnaryOp() {
        return unaryOp;
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return "UnaryOpExpr{" +
                "unaryOp='" + unaryOp + '\'' +
                ", expr=" + expr +
                '}';
    }


    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {

        Type exprType = getExpr().analyzeAndGetType(funcMap,varAndParamMap);
        String unaryOp = getUnaryOp();

        switch (unaryOp){
            case "not":
                if (!exprType.getType().equals("Boolean")){
                    throw new SemanticAnalysisException("Expression type should be Boolean ",getExpr());
                }
                else{
                    return exprType;
                }
            case "-":
                if (!exprType.getType().equals("Integer")){
                    throw new SemanticAnalysisException("Expression type should be Integer",getExpr());
                }
                else {
                    return exprType;
                }
            default:
                throw new SemanticAnalysisException("Something went wrong",getExpr());
        }



//        return exprType;

        //        return null;
    }
}
