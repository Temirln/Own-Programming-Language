package splat.semanticanalyzer;

import java.util.*;

import splat.parser.elements.*;
import splat.parser.elements.DECL.FunctionDecl;
import splat.parser.elements.DECL.VariableDecl;
import splat.parser.elements.STMT.IfThenElseStmt;
import splat.parser.elements.STMT.ReturnExStmt;


public class SemanticAnalyzer {

	private ProgramAST progAST;

	private Map<String, FunctionDecl> funcMap;
	private Map<String, Type> progVarMap;

	public SemanticAnalyzer(ProgramAST progAST) {
		this.progAST = progAST;
	}

	public void analyze() throws SemanticAnalysisException {

		// Checks to make sure we don't use the same labels more than once
		// for our program functions and variables
		checkNoDuplicateProgLabels();

		// This sets the maps that will be needed later when we need to
		// typecheck variable references and function calls in the
		// program body

		setProgVarAndFuncMaps();

		// Perform semantic analysis on the functions
		for (FunctionDecl funcDecl : funcMap.values()) {
			analyzeFuncDecl(funcDecl);
//			System.out.println(funcDecl);
		}

//		// Perform semantic analysis on the program body
		for (Statement stmt : progAST.getStmts()) {
			stmt.analyze(funcMap, progVarMap);
		}

	}

	private void analyzeFuncDecl(FunctionDecl funcDecl) throws SemanticAnalysisException {

		// Checks to make sure we don't use the same labels more than once
		// among our function parameters, local variables, and function names
		checkNoDuplicateFuncLabels(funcDecl);

		// Get the types of the parameters and local variables
		Map<String, Type> varAndParamMap = getVarAndParamMap(funcDecl);

		// Perform semantic analysis on the function body
		for (Statement stmt : funcDecl.getStmts()) {

			if (!funcDecl.getRetType().getType().equals("void") && !(funcDecl.getStmts().get(funcDecl.getStmts().size()-1) instanceof ReturnExStmt || funcDecl.getStmts().get(funcDecl.getStmts().size()-1) instanceof IfThenElseStmt)){
				throw new SemanticAnalysisException("Non-Void Function should return Statement",stmt);
			}

			stmt.analyze(funcMap, varAndParamMap);
		}

	}


	private Map<String, Type> getVarAndParamMap(FunctionDecl funcDecl) {
//
		Map<String, Type> varAndParam = new HashMap<>();

		varAndParam.put("0result",funcDecl.getRetType());

		for (Param params : funcDecl.getParams()){
			varAndParam.put(params.getLabel(),params.getType());
		}

		for (VariableDecl localVariable : funcDecl.getLocalVarDecls()){
			varAndParam.put(localVariable.getLabel(),localVariable.getType());
		}

		// FIXME: Somewhat similar to setProgVarAndFuncMaps()
//		return null;
		return varAndParam;
	}

	private void checkNoDuplicateFuncLabels(FunctionDecl funcDecl) throws SemanticAnalysisException {
		//
		Set<String> labels = new HashSet<String>();

		String funcLabel = funcDecl.getLabel();
		labels.add(funcLabel);

		//	Check Parameters
		for (Param param : funcDecl.getParams()){
			String label = param.getLabel().toString();
			if (labels.contains(label) || funcMap.containsKey(label)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '"
						+ label + "' in program", param);
			} else {
				labels.add(label);
			}
		}

		//Check Local VAriables

		for (VariableDecl localVariable : funcDecl.getLocalVarDecls()){
			String label = localVariable.getLabel().toString();
			if (labels.contains(label) || funcMap.containsKey(label)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '"
						+ label + "' in program", localVariable);
			} else {
				labels.add(label);
			}
		}


		// FIXME: Similar to checkNoDuplicateProgLabels()
	}

	private void checkNoDuplicateProgLabels() throws SemanticAnalysisException {

		Set<String> labels = new HashSet<String>();

 		for (Declaration decl : progAST.getDecls()) {
 			String label = decl.getLabel().toString();

			if (labels.contains(label)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '"
						+ label + "' in program", decl);
			} else {
				labels.add(label);
			}

		}
	}

	private void setProgVarAndFuncMaps() {

		funcMap = new HashMap<>();
		progVarMap = new HashMap<>();

		for (Declaration decl : progAST.getDecls()) {

			String label = decl.getLabel().toString();

			if (decl instanceof FunctionDecl) {
				FunctionDecl funcDecl = (FunctionDecl)decl;
				funcMap.put(label, funcDecl);

			} else if (decl instanceof VariableDecl) {
				VariableDecl varDecl = (VariableDecl)decl;
				progVarMap.put(label, varDecl.getType());
			}
		}
	}
}
