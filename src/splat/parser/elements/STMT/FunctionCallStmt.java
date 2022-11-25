package splat.parser.elements.STMT;

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

    }
}
