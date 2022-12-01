package splat.parser.elements.STMT;

import splat.executor.MIPSCode;
import splat.executor.StackFrameInfo;
import splat.lexer.Token;
import splat.parser.elements.DECL.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class ReturnStmt extends Statement {

    public ReturnStmt(Token tok) {
        super(tok);
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {

        if (!varAndParamMap.containsKey("0result")){
            throw new SemanticAnalysisException("Return statement can only be used in function body",getLine(),getColumn());
        }

        if(!varAndParamMap.get("0result").getType().equals("void")){
            throw new SemanticAnalysisException("Non Void function cannot have simple Return statement",getLine(),getColumn());
        }
    }

    @Override
    public void convertToMIPS(MIPSCode mipsCode, StackFrameInfo frameInfo) {
        mipsCode.append("########################## RETURNSTMT ##########################\n");
        String func_name = frameInfo.getFunctionName();
//        System.out.println(func_name);
//        mipsCode.append("jal "+func_name+"_end\n");
        mipsCode.append("addi $v1,$a0,0\n");
        mipsCode.append("jal "+func_name+"_return\n");
        mipsCode.append("########################## RETURNSTMT ##########################\n");
    }


    @Override
    public String toString() {
        return "ReturnStmt{}";
    }
}
