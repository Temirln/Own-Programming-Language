package splat.parser.elements.STMT;

import splat.executor.LabelGenerator;
import splat.executor.MIPSCode;
import splat.executor.StackFrameInfo;
import splat.lexer.Token;
import splat.parser.elements.DECL.FunctionDecl;
import splat.parser.elements.EXPR.BinaryOpExpr;
import splat.parser.elements.EXPR.LabelExpr;
import splat.parser.elements.EXPR.UnaryOpExpr;
import splat.parser.elements.Expression;
import splat.parser.elements.STMT.ReturnExStmt;
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

    @Override
    public void convertToMIPS(MIPSCode mipsCode, StackFrameInfo frameInfo) {
        Expression expr = getExpr();
        mipsCode.append("######### IFELSETHENSTMT ############\n");

        String if_label = LabelGenerator.getNewIfLabel();

        getExpr().computeAndStore(mipsCode,frameInfo,0);
        mipsCode.append("li $v0,4\n");
        mipsCode.append("la $a1,true_string\n");
        mipsCode.append("beq $a0,$a1,"+if_label+"\n");

        if (getStmts2().size() > 0){
            for(Statement stmt : getStmts2()){
                stmt.convertToMIPS(mipsCode,frameInfo);
            }
        }

        mipsCode.append("j next_"+if_label+"\n");

        mipsCode.append(if_label+":\n");
        for (Statement stmt : getStmts1()){
            stmt.convertToMIPS(mipsCode,frameInfo);
        }
        mipsCode.append("next_"+if_label+":\n\n");

        mipsCode.append("######### IFELSETHENTSTMT ############\n");

    }

}
