package splat.parser.elements.STMT;

import splat.executor.LabelGenerator;
import splat.executor.MIPSCode;
import splat.executor.StackFrameInfo;
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

    @Override
    public void convertToMIPS(MIPSCode mipsCode, StackFrameInfo frameInfo) {

        mipsCode.append("########################## WHILESTMT ##########################\n");

        String while_label = LabelGenerator.getNewWhileLabel();

        mipsCode.append("   li $v0,4\n");
        mipsCode.append("   la $a3,true_string\n");

        mipsCode.append(while_label+":\n");
        getExpr().computeAndStore(mipsCode,frameInfo,0);
        mipsCode.append("   li $v0,4\n");
        mipsCode.append("   la $a1,true_string\n");
        mipsCode.append("   bne $a0,$a1,exit_"+while_label+"\n");

        for(Statement stmt : getStmts()){
            stmt.convertToMIPS(mipsCode,frameInfo);
        }
        mipsCode.append("   j "+while_label+"\n");
        mipsCode.append("   exit_"+while_label+":\n");
        mipsCode.append("########################## WHILESTMT ##########################\n");

    }
}
