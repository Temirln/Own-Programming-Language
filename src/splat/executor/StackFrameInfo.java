package splat.executor;

import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Param;
import splat.parser.elements.VariableDecl;

import java.util.ArrayList;
import java.util.List;

public class StackFrameInfo {

    private String funcName;
    private List<Param> parameters;
    private List<VariableDecl> varDecls;
    private String returnType;

    // This is used for declared functions
    public StackFrameInfo(FunctionDecl funcDecl) {

        funcName = funcDecl.getLabel().toString();
        parameters = funcDecl.getParams();
        varDecls = funcDecl.getLocalVarDecls();
        returnType = funcDecl.getRetType().toString();
    }

    // This is used for the main program
    public StackFrameInfo(String funcName, List<VariableDecl> varDecls) {

        this.funcName = funcName;
        this.parameters = new ArrayList<>();
        this.varDecls = varDecls;
        this.returnType = "void";
    }

    public int getRegisterNum(String varName) {
        int regNum = 0;
        for (Param param : parameters) {
            if (param.getLabel().toString().equals(varName)) {
                return regNum;
            }
            regNum++;
//            System.out.println("here");
        }
        for (VariableDecl varDecl : varDecls) {
            if (varDecl.getLabel().toString().equals(varName)) {
                return regNum;
            }
            regNum++;
//            System.out.println("here");
        }
        // This should never happen
        return 99999;
    }

    public String getFunctionName() {
        return funcName;
    }

    public int numberOfVars() {
        return this.varDecls.size() + this.parameters.size();
    }
}
