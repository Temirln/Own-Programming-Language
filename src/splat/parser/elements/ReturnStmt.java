package splat.parser.elements;

import splat.lexer.Token;
import splat.parser.elements.FunctionDecl;
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
    public String toString() {
        return "ReturnStmt{}";
    }
}
