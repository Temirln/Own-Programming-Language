package splat;

import java.io.File;
import java.util.List;

import splat.executor.Executor;
import splat.lexer.Lexer;
import splat.lexer.Token;
import splat.parser.Parser;
import splat.parser.elements.ProgramAST;
import splat.semanticanalyzer.SemanticAnalyzer;

public class Splat {

	private File progFile;
	
	public Splat(File progFile) {
		this.progFile = progFile;
	}
	
	public void processFileAndExecute() throws SplatException {
		
		//Step 1.  Tokenize
		Lexer lexer = new Lexer(progFile);
		List<Token> tokens = lexer.tokenize();

		// Step 2.  Parse
		Parser parser = new Parser(tokens);
		ProgramAST progAST = parser.parse();

		System.out.println(progAST);
		// Step 3.  Semantic Analysis
		SemanticAnalyzer analyzer = new SemanticAnalyzer(progAST);
		analyzer.analyze();


		// Step 4.  Executor
		Executor executor = new Executor(progAST);
		executor.runProgram();
		
		// THE END!
	}

}
