package splat.parser.elements.STMT;

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

public class FunctionCallStmt extends Statement {
    private final String Label;
    private final List<Expression> args;

    public FunctionCallStmt(Token tok, String label, List<Expression> exprs) {
        super(tok);
        Label = label;
        this.args = exprs;
    }

    public String getLabel() {
        return Label;
    }

    public List<Expression> getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return "FunctionCallStmt{" +
                "Label='" + Label + '\'' +
                ", args=" + args +
                '}';
    }


    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        String funcLabel = getLabel();

        if (!funcMap.containsKey(funcLabel)){
            throw new SemanticAnalysisException("There is no function with given label "+getLabel(),getLine(),getColumn());
        }

        if (!funcMap.get(funcLabel).getRetType().getType().equals("void")){
            throw new SemanticAnalysisException("Function Call Statement should be void for call",getLine(),getColumn());
        }


        if (getArgs().size() != funcMap.get(funcLabel).getParams().size()){
            throw new SemanticAnalysisException("Incorrect number of input parameters ",getArgs().get(0));
        }

        FunctionDecl func = funcMap.get(funcLabel);

        for (int i = 0;i<getArgs().size();i++){

            Type exprType = getArgs().get(i).analyzeAndGetType(funcMap,varAndParamMap);
            if (!func.getParams().get(i).getType().getType().equals(exprType.getType())){
                throw new SemanticAnalysisException("Types of parameters doesn't match",func.getParams().get(i));
            }

        }
    }

    @Override
    public void convertToMIPS(MIPSCode mipsCode, StackFrameInfo frameInfo) {
        mipsCode.append("############ FUNCTIONCALLSTMT ##############\n");

        String label = getLabel();
        for (Expression expr : getArgs()){
            expr.computeAndStore(mipsCode,frameInfo,0);
        }

        mipsCode.append("jal "+label+"\n");
        mipsCode.append("############ FUNCTIONCALLSTMT ##############\n");
    }
}
