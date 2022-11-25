package splat.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import splat.lexer.Token;
import splat.parser.elements.*;
import splat.parser.elements.EXPR.*;
import splat.parser.elements.DECL.FunctionDecl;
import splat.parser.elements.STMT.*;
import splat.parser.elements.DECL.VariableDecl;

public class Parser {

	private List<Token> tokens;

	public Parser(List<Token> tokens) {
		this.tokens = tokens;
	}

	/**
	 * Compares the next token to an expected value, and throws
	 * an exception if they don't match.  This removes the front-most
	 * (next) token  
	 * 
	 * @param expected value of the next token
	 * @throws ParseException if the actual token doesn't match what 
	 * 			was expected
	 */
	private void checkNext(String expected) throws ParseException {
		Token tok = tokens.remove(0);
		if (!tok.getValue().equals(expected)) {
			throw new ParseException("Expected '"+ expected + "', got '" 
					+ tok.getValue()+ "'.", tok);
		}
	}
	
	/**
	 * Returns a boolean indicating whether or not the next token matches
	 * the expected String value.  This does not remove the token from the
	 * token list.
	 * 
	 * @param expected value of the next token
	 * @return true iff the token value matches the expected string
	 */
	private boolean peekNext(String expected) {
		return tokens.get(0).getValue().equals(expected);
	}
	
	/**
	 * Returns a boolean indicating whether or not the token directly after
	 * the front most token matches the expected String value.  This does 
	 * not remove any tokens from the token list.
	 * 
	 * @param expected value of the token directly after the next token
	 * @return true iff the value matches the expected string
	 */
	private boolean peekTwoAhead(String expected) {
		return tokens.get(1).getValue().equals(expected);
	}
	
	
	/*
	 *  <program> ::= program <decls> begin <stmts> end ;
	 */
	public ProgramAST parse() throws ParseException {
		
		try {
			// Needed for 'program' token position info
			Token startTok = tokens.get(0);

			// I added this line
			checkNext("program");
			//	here

			List<Declaration> decls = parseDecls();

			checkNext("begin");

			List<Statement> stmts = parseStmts();
			
			checkNext("end");
			checkNext(";");
	
			return new ProgramAST(decls, stmts, startTok);

		// This might happen if we do a tokens.get(), and nothing is there!
		} catch (IndexOutOfBoundsException ex) {
			
			throw new ParseException("Unexpectedly reached the end of file.", -1, -1);
		}
	}
	
	/*
	 *  <decls> ::= (  <decl>  )*
	 */
	private List<Declaration> parseDecls() throws ParseException {
		
		List<Declaration> decls = new ArrayList<Declaration>();
		
		while (!peekNext("begin")) {
			Declaration decl = parseDecl();
			decls.add(decl);
		}
		
		return decls;
	}
	
	/*
	 * <decl> ::= <var-decl> | <func-decl>
	 */
	private Declaration parseDecl() throws ParseException {

		if (peekTwoAhead(":")) {
			return parseVarDecl();
		} else if (peekTwoAhead("(")) {
			return parseFuncDecl();
		} else {
			Token tok = tokens.get(0);
			throw new ParseException("Declaration expected", tok);
		}
	}
	
	/*
	 * <func-decl> ::= <label> ( <params> ) : <ret-type> is 
	 * 						<loc-var-decls> begin <stmts> end ;
	 */
	private FunctionDecl parseFuncDecl() throws ParseException {
		Token tok = tokens.get(0);

		String[] ret_type = {"Boolean","String","Integer","void"};

		String label = tokens.get(0).getValue();
		if (!Character.isLetter(label.charAt(0))){
			throw new ParseException("Invalid Variable",tokens.get(0));
		}
		checkNext(label);
		checkNext("(");

		List<VariableDecl> params = new ArrayList<VariableDecl>();
		while (!peekNext(")")){
			VariableDecl param = parseParams();
			params.add(param);
			if (!peekNext(")")){
				checkNext(",");
			}
		}

		checkNext(")");
		checkNext(":");

		if(!Arrays.asList(ret_type).contains(tokens.get(0).getValue())){
			throw new ParseException("Incorrect Type",tok);
		}

//		String returnType = tokens.get(0).getValue();
//		checkNext(returnType);

		Type returnType = parseType();
		checkNext("is");

		List<VariableDecl> loc_var_decls = new ArrayList<VariableDecl>();

		while(!peekNext("begin")){
			VariableDecl var_decl = parseVarDecl();
			loc_var_decls.add(var_decl);
		}

		checkNext("begin");

		List<Statement> stmts = parseStmts();

		checkNext("end");
		checkNext(";");


		// TODO Auto-generated method stub
		return new FunctionDecl(tok,label,params,returnType,loc_var_decls,stmts);
	}

	private Type parseType() throws ParseException{

		Token tok = tokens.get(0);

		String type = tokens.get(0).getValue();
		checkNext(type);

		return new Type(tok,type);
	}

	/*
	 * <param> ::= <label> : <type>
	 */
	private VariableDecl parseParams() throws ParseException{
		Token tok = tokens.get(0);

		String label = tokens.get(0).getValue();

		if (!Character.isLetter(label.charAt(0))){
			throw new ParseException("Invalid Variable",tokens.get(0));
		}
		checkNext(label);

		checkNext(":");

		String[] ret_type = {"Boolean","String","Integer"};
		if(!Arrays.asList(ret_type).contains(tokens.get(0).getValue())){
			throw new ParseException("Incorrect Type",tok);
		}
//		String type = tokens.get(0).getValue();
//		checkNext(type);

		Type type = parseType();

		return new VariableDecl(tok,label,type);

	}

	/*
	 * <var-decl> ::= <label> : <type> ;
	 */
	private VariableDecl parseVarDecl() throws ParseException {

		Token tok = tokens.get(0);
		String[] types = {"Boolean","String","Integer"};

		String label = tokens.get(0).getValue();
		if(!Character.isLetter(label.charAt(0))){
			throw new ParseException("Invalid Variable",tokens.get(0));
		}
		checkNext(label);

		checkNext(":");


		if(!Arrays.asList(types).contains(tokens.get(0).getValue())){
			throw new ParseException("Incorrect Type",tokens.get(0));
		}
//		String type = tokens.get(0).getValue();
//		checkNext(type);
		Type type = parseType();

		checkNext(";");


		// TODO Auto-generated method stub
		return new VariableDecl(tok,label,type);
	}

	/*
	 * <stmts> ::= (  <stmt>  )*
	 */
	private List<Statement> parseStmts() throws ParseException {

		List<Statement> stmts = new ArrayList<Statement>();

		while (!peekNext("end")) {
			Statement stmt = parseStmt();
			stmts.add(stmt);
		}

		return stmts;


		// TODO Auto-generated method stub
	}





	//////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * <stmt> ::= <label> := <expr> ;
				| while <expr> do <stmts> end while ;
				| if <expr> then <stmts> else <stmts> end if ;
				| if <expr> then <stmts> end if ;
				| <label> ( <args> ) ;
				| print <expr> ;
				| print_line ;
				| return <expr> ;
				| return ;
	 */
	private Statement parseStmt() throws ParseException {
		if (peekTwoAhead(":=")) {
			return parseAssignStmt();
		}
		else if (peekNext("return") && peekTwoAhead(";")) {
			return parseReturnStmt();
		}
		else if (peekNext("return")) {
			return parseReturnExStmt();
		}
		else if (peekNext("if")) {
			return parseIfThenElseStmt();
		}
		else if (peekNext("while")) {
			return parseWhileLoopStmt();
		}
		else if (peekNext("print")) {
			return parsePrintStmt();
		}
		else if (peekNext("print_line")) {
			return parsePrintLineStmt();
		}
		else if (peekTwoAhead("(")) {
			return parseFunctionStmt();
		}

		else {
			Token tok = tokens.get(0);
			throw new ParseException("Statement expected", tok);
		}

	}

	/*
	 * <stmt> ::= <label> := <expr> ;
	 */
	private AssignVariableStmt parseAssignStmt() throws ParseException{
		Token tok = tokens.get(0);

		String label = tokens.get(0).getValue();

		checkNext(label);

		checkNext(":=");

		Expression expr = parseExpr();

		checkNext(";");

		return new AssignVariableStmt(tok,label,expr);

	}


	/*
	 * (stmt) ::= <label> ( <args> ) ;
	 */
	private FunctionCallStmt parseFunctionStmt() throws ParseException{
		Token tok = tokens.get(0);

		String label = tokens.get(0).getValue();
		checkNext(label);
		checkNext("(");

		List<Expression> args = new ArrayList<Expression>();

		while(!peekNext(")")){
			Expression expr = parseExpr();
			args.add(expr);
			if(peekNext(",")){
				checkNext(",");
			}

		}
		checkNext(")");
		checkNext(";");

		return new FunctionCallStmt(tok,label,args);

	}


	/*
	 * (stmt) ::= return <expr> ;
	 */
	private ReturnExStmt parseReturnExStmt() throws ParseException{
		Token tok = tokens.get(0);

		checkNext("return");

		Expression expr = parseExpr();

		checkNext(";");

		return new ReturnExStmt(tok,expr);
	}

	/*
	 * (stmt) ::= return ;
	 */
	private ReturnStmt parseReturnStmt() throws ParseException{
		Token tok = tokens.get(0);
		checkNext(tok.getValue());
		checkNext(";");

		return new ReturnStmt(tok);
	}


	/*
	 * (stmt) ::= print <expr> ;
	 */
	private PrintStmt parsePrintStmt() throws ParseException{
		Token tok = tokens.get(0);
		checkNext("print");

		Expression expr = parseExpr();

		checkNext(";");

		return new PrintStmt(tok,expr);
	}

	/*
	 * (stmt) ::= print_line ;
	 */
	private PrintLineStmt parsePrintLineStmt() throws ParseException{
		Token tok = tokens.get(0);
		checkNext(tok.getValue());
		checkNext(";");
		return new PrintLineStmt(tok);
	}

	/*
	 * (stmt) ::= if <expr> then <stmts> else <stmts> end if ;
				| if <expr> then <stmts> end if ;
	 */
	private IfThenElseStmt parseIfThenElseStmt() throws ParseException{
		Token tok = tokens.get(0);
		checkNext("if");

		Expression expr = parseExpr();
		checkNext("then");

		List<Statement> stmts1 = new ArrayList<Statement>();
		while(!peekNext("end")){
			if (peekNext("else")){
				break;
			}
			Statement stmt = parseStmt();
			stmts1.add(stmt);


		}

		List<Statement> stmts2 = new ArrayList<Statement>();
		if (peekNext("else")){
			checkNext("else");
			while(!peekNext("end")){
				Statement stmt = parseStmt();
				stmts2.add(stmt);

			}

		}

		checkNext("end");
		checkNext("if");
		checkNext(";");

		return new IfThenElseStmt(tok,expr,stmts1,stmts2);
	}

	/*
	 * (stmt) ::= while <expr> do <stmts> end while ;
	 */
	private WhileLoopStmt parseWhileLoopStmt() throws ParseException{
		Token tok = tokens.get(0);

		checkNext("while");

		Expression expr = parseExpr();

		checkNext("do");

		List<Statement> stmts = new ArrayList<Statement>();
		while(!peekNext("end")){
			Statement stmt = parseStmt();
			stmts.add(stmt);
		}

		checkNext("end");

		checkNext("while");
		checkNext(";");

		return new WhileLoopStmt(tok,expr,stmts);
	}


	/*
	 * <expr> ::= ( <expr> <bin-op> <expr> )
				| ( <unary-op> <expr> )
				| <label> ( <args> )
				| <label>
				| <literal>
	 */
	private Expression parseExpr() throws ParseException{

		String[] unaryOp = {"not","-"};
		String[] boolLiteral = {"true","false"};

		if (peekNext("(")){
			checkNext("(");
			if(Arrays.asList(unaryOp).contains(tokens.get(0).getValue())){
				return parseUnaryOpExpr();
			}
			else{
				return parseBinaryOpExpr();
			}
		}

		else if(peekTwoAhead("(")){
			return parseFunctionExpr();
		}

		else if(tokens.get(0).getValue().charAt(0) == '\"'){
			return parseStringLiteralExpr();
		}
		else if(Arrays.asList(boolLiteral).contains(tokens.get(0).getValue())){
			return parseBoolLiteralExpr();
		}

		else if(Character.isDigit(tokens.get(0).getValue().charAt(0))){
			return parseIntLiteralExpr();
		}

		else if(Character.isLetter(tokens.get(0).getValue().charAt(0))){
			return parseLabelExpr();
		}

		else{
			Token tok = tokens.get(0);
			throw new ParseException("Statement expected", tok);
		}

	}

	/*
	 * (expr) ::= <label>
 	 */
	private LabelExpr parseLabelExpr() throws ParseException{
		Token tok = tokens.get(0);
		String label = tokens.get(0).getValue();
		checkNext(label);
		return new LabelExpr(tok,label);
	}

	/*
	 * (expr) ::= ( <expr> <bin-op> <expr> )
	 */
	private BinaryOpExpr parseBinaryOpExpr() throws ParseException{
		String[] binaryOperations = {"and","or",">","<", "==",">=","<=","+","-","*","/","%"};

		Token tok = tokens.get(0);
		Expression expr1 = parseExpr();


		String binaryOp = tokens.get(0).getValue();
		if(!Arrays.asList(binaryOperations).contains(binaryOp)){
			throw new ParseException("Non Valid Operator",tok);
		}
		checkNext(binaryOp);

		Expression expr2 = parseExpr();

		checkNext(")");
		return new BinaryOpExpr(tok,expr1,expr2,binaryOp);
	}

	/*
	 * (expr) ::= ( <unary-op> <expr> )
	 */
	private UnaryOpExpr parseUnaryOpExpr() throws ParseException{
		Token tok = tokens.get(0);

		String unaryOp = tokens.get(0).getValue();
		checkNext(unaryOp);

		Expression expr = parseExpr();
		checkNext(")");


		return new UnaryOpExpr(tok,unaryOp,expr);
	}


	private IntLiteralExpr parseIntLiteralExpr() throws ParseException{
		Token tok = tokens.get(0);
		String int_literal = tokens.get(0).getValue();
		checkNext(int_literal);
		return new IntLiteralExpr(tok,int_literal);
	}

	private StringLiteralExpr parseStringLiteralExpr() throws ParseException{
		Token tok = tokens.get(0);
		String string_literal = tokens.get(0).getValue();
		checkNext(string_literal);

		return new StringLiteralExpr(tok,string_literal);
	}

	private BoolLiteralExpr parseBoolLiteralExpr() throws ParseException{
		Token tok = tokens.get(0);

		String bool_literal = tokens.get(0).getValue();
		checkNext(bool_literal);

		return new BoolLiteralExpr(tok,bool_literal);
	}

	/*
	 * (expr) ::= <label> ( <args> )
	 */
	private FunctionCallExpr parseFunctionExpr() throws ParseException{
		Token tok = tokens.get(0);

		String label = tokens.get(0).getValue();
		if (!Character.isLetter(label.charAt(0))) {
			throw new ParseException("Invalid Label",tok);
		}
		checkNext(label);

		checkNext("(");
		List<Expression> args = new ArrayList <Expression>();
		while (!peekNext(")")){
			Expression expr = parseExpr();
			args.add(expr);
			if (!peekNext(")")){
				checkNext(",");
			}
		}
		checkNext(")");

		return new FunctionCallExpr(tok,label,args);
	}

}