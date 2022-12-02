package splat.parser.elements.EXPR;

import splat.executor.MIPSCode;
import splat.executor.StackFrameInfo;
import splat.lexer.Token;
import splat.parser.elements.DECL.FunctionDecl;
import splat.parser.elements.Expression;
import splat.parser.elements.Param;
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

        for (int i = 0;i<getArgs().size();i++){

            Type exprType = getArgs().get(i).analyzeAndGetType(funcMap,varAndParamMap);

            if (!func.getParams().get(i).getType().getType().equals(exprType.getType())){
                throw new SemanticAnalysisException("Types of parameters doesn't match",func.getParams().get(i));
            }

        }



        return func.getRetType();
    }

    @Override
    public void computeAndStore(MIPSCode mipsCode, StackFrameInfo frameInfo, int regnum) {
        mipsCode.append("################ FUNCTIONCALLEXPR ################\n");



        //Store Values to Stack
//        if (getArgs().size()>0){
//            mipsCode.append("addi $sp, $sp, -"+(frameInfo.numberOfVars()*4)+"\n");
//            for (int i =0; i<frameInfo.numberOfVars();i++){
//                mipsCode.append("sw $t"+i+","+(i*4)+"($sp)\n");
//            }
//        }





        int regcount = 0;
        for(Expression expr : getArgs()){
            expr.computeAndStore(mipsCode,frameInfo,regcount);
            regcount++;
        }





        String label = getLabel();

//        mipsCode.append("   addi $sp, $sp, -12\n");
//        mipsCode.append("   sw $ra, 8($sp)\n");
        mipsCode.append("   jal "+label+"\n");

//        mipsCode.append("   addi $a0,$v1,0\n");


//        mipsCode.append("   lw $ra, 8($sp)\n");
//        mipsCode.append("   addi $sp, $sp, 12\n");


        mipsCode.append("   addi $a"+regnum+",$v1,0\n");
//
//        mipsCode.append("addi $sp,$sp -4\n");
//        mipsCode.append("sw $a"+regnum+",0($sp)\n");



        //Load Values from the stack
//        if (getArgs().size()>0){
//
//            for (int i =0; i<frameInfo.numberOfVars();i++){
//                mipsCode.append("lw $t"+i+","+(i*4)+"($sp)\n");
//            }
//            mipsCode.append("addi $sp, $sp, "+(frameInfo.numberOfVars()*4)+"\n");
//        }



//        mipsCode.append("lw $a"+regnum+",0($sp)\n");
//        mipsCode.append("addi $sp,$sp 4\n");


        mipsCode.append("################ FUNCTIONCALLEXPR ################\n");
    }
}
