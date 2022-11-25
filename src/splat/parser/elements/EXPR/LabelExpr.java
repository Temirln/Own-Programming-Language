package splat.parser.elements.EXPR;

import splat.lexer.Token;
import splat.parser.elements.DECL.FunctionDecl;
import splat.parser.elements.Expression;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

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
        return null;
    }
}
