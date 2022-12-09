package splat.parser.elements;

import splat.executor.LabelGenerator;
import splat.executor.MIPSCode;
import splat.executor.StackFrameInfo;
import splat.lexer.Token;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Expression;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class StringLiteralExpr extends Expression {

    private final String string_literal;

    public StringLiteralExpr(Token tok, String string_literal) {
        super(tok);
        this.string_literal = string_literal;
    }



    public String getString_literal() {
        return string_literal;
    }

    @Override
    public String toString() {
        return "StringLiteralExpr{" +
                "string_literal='" + string_literal + '\'' +
                '}';
    }


    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Token tok = new Token(getString_literal(),getColumn(),getLine());

        Type stringLiteralType = new Type(tok,"String");

        return stringLiteralType;
    }

    @Override
    public void computeAndStore(MIPSCode mipsCode, StackFrameInfo frameInfo, int regnum) {
//        mipsCode.append("li $s"+regnum+",4\n");
        mipsCode.append("################ STRINGLITERAL ################\n");
        mipsCode.append("   li $v0,4\n");

        String label =LabelGenerator.getNewStringLabel();
//        System.out.println(label);
        mipsCode.addNewStringLiteral(label,getString_literal());
        mipsCode.append("   la $a0,"+label+"\n");
        mipsCode.append("################ STRINGLITERAL ################\n");
    }
}
