package splat.parser.elements.DECL;

import splat.executor.MIPSCode;
import splat.executor.StackFrameInfo;
import splat.lexer.Token;
import splat.parser.elements.*;

import java.util.List;

public class FunctionDecl extends Declaration {

	// Need to add some fields
//	private final String label;
	private final List<Param> params;
	private final Type retType;
	private final List<VariableDecl> loc_var_decls;
	private final List<Statement> stmts;


	public List<Param> getParams() {
		return params;
	}

	// Need to add extra arguments for setting fields in the constructor
	public FunctionDecl(Token tok, String label, List<Param> params, Type retType, List<VariableDecl> loc_var_decls, List<Statement> stmts) {
		super(tok, label);
//		this.label = label;
		this.params = params;

		this.retType = retType;
		this.loc_var_decls = loc_var_decls;
		this.stmts = stmts;
	}

//	public String getLabel() {
//		return label;
//	}

	public Type getRetType() {
		return retType;
	}

	public List<VariableDecl> getLocalVarDecls() {
		return loc_var_decls;
	}

	public List<Statement> getStmts() {
		return stmts;
	}


	// Getters?


	@Override
	public String toString() {
		return "FunctionDecl{" +
				"label='" + getLabel() + '\'' +
				", params=" + params +
				", retType='" + retType + '\'' +
				", loc_var_decls=" + loc_var_decls +
				", stmts=" + stmts +
				'}';
	}

	public void convertToMIPS(MIPSCode mipsCode){
		mipsCode.append("############################### FUNCTIONDECL ################################\n");
		StackFrameInfo frameInfo = new StackFrameInfo(FunctionDecl.this);
//		System.out.println(frameInfo.getFunctionName());

		mipsCode.append(getLabel() + ": nop\n");

		for(VariableDecl locVar : getLocalVarDecls()){

			mipsCode.append("addi $t"+frameInfo.getRegisterNum(locVar.getLabel())+",$zero,0\n");
		}

		for (Param param : getParams()){
//			mipsCode.append("$sp,$sp,-4");
//			mipsCode.append("sw $s");
//			mipsCode.append("move $v0"+"$s"+frameInfo.);
			mipsCode.append("move $t"+frameInfo.getRegisterNum(param.getLabel())+",$a"+frameInfo.getRegisterNum(param.getLabel())+"\n");
		}


		for (Statement stmt : getStmts()){
			stmt.convertToMIPS(mipsCode,frameInfo);
		}


		mipsCode.append(getLabel() + "_end: jr $ra\n");
//		mipsCode.append("\n");
		mipsCode.append("############################### FUNCTIONDECL ################################\n");
	}

}
