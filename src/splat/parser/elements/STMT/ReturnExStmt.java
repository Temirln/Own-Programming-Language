package splat.parser.elements.STMT;

import splat.lexer.Token;
import splat.parser.elements.DECL.FunctionDecl;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class ReturnExStmt extends Statement {

    private final Expression expr;

    public ReturnExStmt(Token tok, Expression expr) {
        super(tok);
        this.expr = expr;
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return "ReturnExExpr{" +
                "expr=" + expr +
                '}';
    }


    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type exprType = getExpr().analyzeAndGetType(funcMap,varAndParamMap);

        if (!varAndParamMap.containsKey("0result")){
            throw new SemanticAnalysisException("Return statement can only be used in function body",getLine(),getColumn());
        }

        if (varAndParamMap.get("0result").getType().equals("void")){
            throw new SemanticAnalysisException("Void Function cannot have a statement of the form return <expr>",getExpr());
        }

        if (!exprType.getType().equals(varAndParamMap.get("0result").getType())){
            throw new SemanticAnalysisException("Return type should match with function returnType",getExpr());
        }

    }
}
