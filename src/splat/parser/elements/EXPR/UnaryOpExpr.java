package splat.parser.elements.EXPR;

import splat.executor.LabelGenerator;
import splat.executor.MIPSCode;
import splat.executor.StackFrameInfo;
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

    @Override
    public void computeAndStore(MIPSCode mipsCode, StackFrameInfo frameInfo, int regnum) {
        mipsCode.append("################ UNARYOP ################\n");
        Expression expression = getExpr();
        getExpr().computeAndStore(mipsCode,frameInfo,0);
        String unaryOp = getUnaryOp();
        String general_label = LabelGenerator.getNewGeneralLabel();
        switch (unaryOp) {
            case "not" -> {
                mipsCode.append("   li $v0,4\n");
                mipsCode.append("   la $a3,true_string\n");
                mipsCode.append("   beq $a" + regnum + ",$a3,swap_" + general_label + "\n");
                mipsCode.append("   la $a0,true_string\n");
                mipsCode.append("   j next_" + general_label + "\n");
                mipsCode.append("   swap_" + general_label + ":\n");
                mipsCode.append("   li $v0,4\n");
                mipsCode.append("   la $a0,false_string\n");
                mipsCode.append("   next_" + general_label + ":\n");
            }
            case "-" -> mipsCode.append("   sub $a0,$zero,$a0\n");
        }
        mipsCode.append("################ UNARYOP ################\n");
    }
}
