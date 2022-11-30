package splat.parser.elements.STMT;

import splat.executor.MIPSCode;
import splat.executor.StackFrameInfo;
import splat.lexer.Token;
import splat.parser.elements.DECL.FunctionDecl;
import splat.parser.elements.EXPR.LabelExpr;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class PrintStmt extends Statement {

    private final Expression expr;

    public PrintStmt(Token tok, Expression expr) {
        super(tok);
        this.expr = expr;
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return "PrintStmt{" +
                "expr=" + expr +
                '}';
    }


    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        getExpr().analyzeAndGetType(funcMap,varAndParamMap);
    }

    @Override
    public void convertToMIPS(MIPSCode mipsCode, StackFrameInfo frameInfo) {
        int regnum = frameInfo.numberOfVars();
        mipsCode.append("######### PRINTSTMT ############\n");

        Expression expr = getExpr();

        if (expr instanceof LabelExpr){
            regnum = frameInfo.getRegisterNum(((LabelExpr) expr).getLabel());
        }

        getExpr().computeAndStore(mipsCode,frameInfo, 0);
        mipsCode.append("syscall\n");
        mipsCode.append("######### PRINTSTMT ############\n");
    }
}
