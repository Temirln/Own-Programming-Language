package splat.semanticanalyzer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import splat.Splat;
import splat.lexer.LexException;
import splat.lexer.Lexer;
import splat.lexer.Token;
import splat.parser.ParseException;
import splat.parser.Parser;
import splat.parser.elements.ProgramAST;

public class SemanticAnalyzerTester {

	private File testDir;

	private boolean verbose;
	private int testCount;
	private int success;
	private int failure;

	public static void main(String[] args) {

		SemanticAnalyzerTester tester = new SemanticAnalyzerTester(true);
		tester.runTests();
	}

	public SemanticAnalyzerTester(boolean seeResults) {
		verbose = seeResults;
		testCount = 0;
		success = 0;
		failure = 0;
	}

	public void runTests() {

		// First, we get the testing files

		testDir = new File("tests");

		System.out.print("Opening test directory...");

		if (!testDir.exists() || !testDir.isDirectory()) {
			System.out.println("error!");
			System.out.println("Cannot find directory 'tests'.");
			System.out.println("Please create one in your project folder, and add" + " the appropriate testing files.");

			return;
		}

		File[] testFiles = testDir.listFiles();

		System.out.println("success");

		// Now, we run the tests

		System.out.println("Running tests...");

		for (File testFile : testFiles) {
			// Make sure we only test .splat files that should pass parsing
			if (testFile.getName().endsWith("_badsemantics.splat") 
					|| testFile.getName().endsWith("_goodsemantics.splat")) {
				try {
					runTest(testFile);
				} catch (Exception e) {
					// This really shouldn't happen 
					e.printStackTrace();
				}
			}
		}

		// Finally, we output the results

		System.out.println("---------------------------------------");
		System.out.println("FINAL SEMANTIC ANALYSIS TESTING RESULTS");
		System.out.println("---------------------------------------");
		System.out.println("Tests completed: " + testCount);
		System.out.println("Tests succeeded: " + success);
		System.out.println("Tests falied:    " + failure);
	}

	private void runTest(File testFile) throws IOException, LexException, 
															ParseException {

		Lexer lexer = new Lexer(testFile);
		List<Token> tokenz = lexer.tokenize();
		Parser parser = new Parser(tokenz);
		ProgramAST prog = parser.parse(); 
		
		boolean expectSemanticError = testFile.getName().endsWith("_badsemantics.splat");
		
		try {

			testCount++;
			System.out.print("Test Case " + testCount + ": " + testFile.getName() + "...");

			// Run the analyzer on the program AST
			SemanticAnalyzer analyzer = new SemanticAnalyzer(prog);
			analyzer.analyze();
			
			// If we successfully get to here, no semantic analysis exceptions were thrown

			// If we expected an error, but an exception wasn't thrown...fail
			if (expectSemanticError) {

				failure++;
				System.out.println("failed - semantic error should have been detected");
				
				System.out.println(prog);
				
			// No exceptions thrown with no semantic errors in the file...success
			} else {
				
				success++;
				System.out.println("passed (semantic analysis successful)");
				
				if (verbose) {
					System.out.println(prog);
				}
			}

		} catch (SemanticAnalysisException ex) {

			// If a semantic error is what was expected...success
			if (expectSemanticError) {
				success++;
				System.out.println("passed (semantic error caught)");
				if (verbose) {
					System.out.println(ex.getMessage());
					System.out.println();
				}

			// If an exception was thrown, but it shouldn't have been...fail
			} else {
				failure++;
				System.out.println("failed - semantic analysis should have been successful");
				System.out.println(ex.getMessage());
				System.out.println();
			}

		}
	}
	
}
