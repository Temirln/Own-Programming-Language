package splat.parser.elements.STMT;

import splat.executor.MIPSCode;
import splat.executor.StackFrameInfo;
import splat.lexer.Token;
import splat.parser.elements.DECL.FunctionDecl;
import splat.parser.elements.EXPR.StringLiteralExpr;
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

    @Override
    public void convertToMIPS(MIPSCode mipsCode, StackFrameInfo frameInfo) {
//        System.out.println(getExpr());
        mipsCode.append("######### ASSIGNVARIABLESTMT ##########\n");
        getExpr().computeAndStore(mipsCode,frameInfo,0);
//        System.out.println("Regnum "+frameInfo.getRegisterNum(getLabel()));
//        System.out.println(frameInfo.numberOfVars());
//        System.out.println(getLabel());
//        System.out.println(getExpr());
//        if ()
//        Expression expr = getExpr();
//        if (expr){
//            mipsCode.append("li $v0, 4\n");
//
//        }
        int reg = frameInfo.getRegisterNum(getLabel());
        mipsCode.append("move $s"+reg+",$v0\n");
        mipsCode.append("move $t" + reg + ",$a0\n\n");

        mipsCode.append("######### ASSIGNVARIABLESTMT ############\n");
    }
}
