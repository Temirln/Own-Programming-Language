package splat.semanticanalyzer;

import java.util.*;

import splat.parser.elements.Declaration;
import splat.parser.elements.DECL.FunctionDecl;
import splat.parser.elements.ProgramAST;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.parser.elements.DECL.VariableDecl;


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

		System.out.println("\n");
		System.out.println(progAST);
		System.out.println("\n");

		// This sets the maps that will be needed later when we need to
		// typecheck variable references and function calls in the
		// program body
		setProgVarAndFuncMaps();

		System.out.println("FuncMap "+funcMap);
		// Perform semantic analysis on the functions
		for (FunctionDecl funcDecl : funcMap.values()) {
			analyzeFuncDecl(funcDecl);
//			System.out.println(funcDecl);
		}

//		System.out.println("ProgVarMap "+ progVarMap);

//		// Perform semantic analysis on the program body
//		for (Statement stmt : progAST.getStmts()) {
//			stmt.analyze(funcMap, progVarMap);
//		}
	}

	private void analyzeFuncDecl(FunctionDecl funcDecl) throws SemanticAnalysisException {

		// Checks to make sure we don't use the same labels more than once
		// among our function parameters, local variables, and function names
		checkNoDuplicateFuncLabels(funcDecl);

		// Get the types of the parameters and local variables
		Map<String, Type> varAndParamMap = getVarAndParamMap(funcDecl);
		System.out.println("Var And ParamMap "+varAndParamMap);

		// Perform semantic analysis on the function body
		for (Statement stmt : funcDecl.getStmts()) {
//			System.out.println(stmt);
			stmt.analyze(funcMap, varAndParamMap);
		}

	}


	private Map<String, Type> getVarAndParamMap(FunctionDecl funcDecl) {
//		MyFunc
		Map<String, Type> varAndParam = new HashMap<>();

		for (VariableDecl params : funcDecl.getParams()){
			varAndParam.put(params.getLabel(),params.getType());
		}

		for (VariableDecl localVariable : funcDecl.getLoc_var_decls()){
			varAndParam.put(localVariable.getLabel(),localVariable.getType());
		}

		// FIXME: Somewhat similar to setProgVarAndFuncMaps()
//		return null;
		return varAndParam;
	}

	private void checkNoDuplicateFuncLabels(FunctionDecl funcDecl) throws SemanticAnalysisException {
		//My func
		Set<String> labels = new HashSet<String>();

		String funcLabel = funcDecl.getLabel();
		labels.add(funcLabel);

		//	Check Parameters
		for (VariableDecl variableDecl : funcDecl.getParams()){
			String label = variableDecl.getLabel().toString();
			if (labels.contains(label)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '"
						+ label + "' in program", variableDecl);
			} else {
				labels.add(label);
			}
		}

		//Check Local VAriables

		for (VariableDecl localVariable : funcDecl.getLoc_var_decls()){
			String label = localVariable.getLabel().toString();
			if (labels.contains(label)) {
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
