package splat.parser.elements;

import splat.lexer.Token;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Expression;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.List;
import java.util.Map;

public class FunctionCallExpr extends Expression {

    private final String label;
    private final List<Expression> args;

    public FunctionCallExpr(Token tok, String label, List<Expression> args) {
        super(tok);
        this.label = label;
        this.args = args;
    }

    public String getLabel() {
        return label;
    }

    public List<Expression> getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return "FunctionCallExpr{" +
                "label='" + label + '\'' +
                ", args=" + args +
                '}';
    }


    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {

        String funcLabel = getLabel();

        if (!funcMap.containsKey(funcLabel)){
            throw new SemanticAnalysisException("There is no function with given label "+getLabel(),getArgs().get(0));
        }

        if (funcMap.get(funcLabel).getRetType().getType().equals("void")){
            throw new SemanticAnalysisException("Function Call Expression cannot be void",getArgs().get(0));
        }


        if (getArgs().size() != funcMap.get(funcLabel).getParams().size()){
            throw new SemanticAnalysisException("Incorrect number of input parameters ",getArgs().get(0));
        }

        FunctionDecl func = funcMap.get(funcLabel);
        System.out.println(getArgs());
        System.out.println(func.getParams());
        for (int i = 0;i<getArgs().size();i++){

            Type exprType = getArgs().get(i).analyzeAndGetType(funcMap,varAndParamMap);

            if (!func.getParams().get(i).getType().getType().equals(exprType.getType())){
                throw new SemanticAnalysisException("Types of parameters doesn't match",func.getParams().get(i));
            }

        }



        return func.getRetType();
    }
}
