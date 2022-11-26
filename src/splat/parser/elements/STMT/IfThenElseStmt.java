package splat.parser.elements.STMT;

import splat.lexer.Token;
import splat.parser.elements.DECL.FunctionDecl;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.List;
import java.util.Map;

public class IfThenElseStmt extends Statement {

    private final Expression expr;
    private final List<Statement> stmts1;
    private final List<Statement> stmts2;

    public IfThenElseStmt(Token tok, Expression expr, List<Statement> stmts1, List<Statement> stmts2) {
        super(tok);
        this.expr = expr;
        this.stmts1 = stmts1;
        this.stmts2 = stmts2;
    }

    public Expression getExpr() {
        return expr;
    }

    public List<Statement> getStmts1() {
        return stmts1;
    }

    public List<Statement> getStmts2() {
        return stmts2;
    }


    @Override
    public String toString() {
        return "IfThenElseStmt{" +
                "expr=" + expr +
                ", stmts1=" + stmts1 +
                ", stmts2=" + stmts2 +
                '}';
    }


    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {

        Type exprType = getExpr().analyzeAndGetType(funcMap,varAndParamMap);

        if (!exprType.getType().equals("Boolean")){
            throw new SemanticAnalysisException("Expression Type should Boolean",getExpr());
        }

        for(Statement stmt : getStmts1()){
            if (varAndParamMap.containsKey("0result")){
                if (!varAndParamMap.get("0result").getType().equals("void") && !(getStmts1().get(getStmts1().size()-1) instanceof ReturnExStmt)){
                    throw new SemanticAnalysisException("Non-Void Function should return Statement",stmt);
                }
            }

            stmt.analyze(funcMap,varAndParamMap);
        }

        for (Statement stmt : getStmts2()){
            if (varAndParamMap.containsKey("0result")){
                if (!varAndParamMap.get("0result").getType().equals("void") && !(getStmts2().get(getStmts2().size()-1) instanceof ReturnExStmt)){
                    throw new SemanticAnalysisException("Non-Void Function should return Statement",stmt);
                }
            }

            stmt.analyze(funcMap,varAndParamMap);
        }


    }

}
