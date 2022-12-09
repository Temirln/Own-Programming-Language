package splat.parser.elements;

import splat.executor.MIPSCode;
import splat.executor.StackFrameInfo;
import splat.lexer.Token;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class PrintLineStmt extends Statement {

    public PrintLineStmt(Token tok) {
        super(tok);
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {

    }

    @Override
    public void convertToMIPS(MIPSCode mipsCode, StackFrameInfo frameInfo) {
        mipsCode.append("########################## PRINTLINESTMT ##########################\n");
        mipsCode.append("   li $v0,4\n");
        mipsCode.append("   la $a0, new_line_string\n");
        mipsCode.append("   syscall\n");
        mipsCode.append("########################## PRINTLINESTMT ##########################\n");
    }


    @Override
    public String toString() {
        return "PrintLineStmt{}";
    }
}
