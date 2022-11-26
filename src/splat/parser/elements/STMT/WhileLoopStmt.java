package splat.parser.elements.STMT;

import splat.lexer.Token;
import splat.parser.elements.DECL.FunctionDecl;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.List;
import java.util.Map;

public class WhileLoopStmt extends Statement {

    private final Expression expr;
    private final List<Statement> stmts;

    public WhileLoopStmt(Token tok, Expression expr, List<Statement> stmts) {
        super(tok);
        this.expr = expr;
        this.stmts = stmts;
    }

    public Expression getExpr() {
        return expr;
    }

    public List<Statement> getStmts() {
        return stmts;
    }

    @Override
    public String toString() {
        return "WhileLoopStmt{" +
                "expr=" + expr +
                ", stmts=" + stmts +
                '}';
    }


    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type exprType = getExpr().analyzeAndGetType(funcMap,varAndParamMap);

        if (!exprType.getType().equals("Boolean")){
            throw new SemanticAnalysisException("Expression Type should be Boolean",getExpr());
        }

        for (Statement stmt : getStmts()){
            stmt.analyze(funcMap,varAndParamMap);
        }


    }
}
