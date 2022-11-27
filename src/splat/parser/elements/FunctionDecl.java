package splat.parser.elements;

import splat.lexer.Token;

import java.util.List;

public class FunctionDecl extends Declaration {

	// Need to add some fields
//	private final String label;
	private final List<VariableDecl> params;
	private final Type retType;
	private final List<VariableDecl> loc_var_decls;
	private final List<Statement> stmts;

	public List<VariableDecl> getParams() {
		return params;
	}

	// Need to add extra arguments for setting fields in the constructor
	public FunctionDecl(Token tok, String label, List<VariableDecl> params, Type retType, List<VariableDecl> loc_var_decls, List<Statement> stmts) {
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

	public List<VariableDecl> getLoc_var_decls() {
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
}
