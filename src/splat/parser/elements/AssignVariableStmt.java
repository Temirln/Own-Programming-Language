package splat.parser.elements;

import splat.lexer.Token;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class AssignVariableStmt extends Statement {

    private final String label;
    private final Expression expr;

    public AssignVariableStmt(Token tok, String label, Expression expr) {
        super(tok);
        this.label = label;
        this.expr = expr;
    }

    public String getLabel() {
        return label;
    }


    public Expression getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return "AssignVariableStmt{" +
                "label='" + label + '\'' +
                ", expr=" + expr +
                '}';
    }


    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {


        if (!varAndParamMap.containsKey(getLabel())){
            throw new SemanticAnalysisException("Variable Not Defined",getExpr());
        }

        Type labelType = varAndParamMap.get(getLabel());
        Type exprType = getExpr().analyzeAndGetType(funcMap,varAndParamMap);
        if (!labelType.getType().equals(exprType.getType())){
            throw new SemanticAnalysisException("Variable Type "+labelType.getType()+" but Expression Type "+exprType.getType(),getExpr());
        }

    }
}
