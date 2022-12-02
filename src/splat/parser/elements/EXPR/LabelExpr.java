package splat.parser.elements.EXPR;

import splat.executor.LabelGenerator;
import splat.executor.MIPSCode;
import splat.executor.StackFrameInfo;
import splat.lexer.Token;
import splat.parser.elements.DECL.FunctionDecl;
import splat.parser.elements.Expression;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import javax.management.remote.rmi.RMIConnectionImpl;
import java.util.Map;

public class LabelExpr extends Expression {

    private final String label;

    @Override
    public String toString() {
        return "LabelExpr{" +
                "label='" + label + '\'' +
                '}';
    }

    public String getLabel() {
        return label;
    }

    public LabelExpr(Token tok, String label) {
        super(tok);
        this.label = label;
    }


    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {

        if (!varAndParamMap.containsKey(getLabel())){
            throw new SemanticAnalysisException("Variable doesn't defined",getLine(),getColumn());
        }
        return varAndParamMap.get(getLabel());

    }

    @Override
    public void computeAndStore(MIPSCode mipsCode, StackFrameInfo frameInfo, int regnum) {
        mipsCode.append("################ LABELEXPR ################\n");
        int reg = frameInfo.getRegisterNum(getLabel());
        mipsCode.append("   addi $a"+regnum+",$t"+reg+",0\n");
        mipsCode.append("   move $v0,$s"+reg+"\n");
        mipsCode.append("################ LABELEXPR ################\n");
    }
}
