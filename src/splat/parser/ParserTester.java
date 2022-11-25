package splat.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import splat.lexer.LexException;
import splat.lexer.Lexer;
import splat.lexer.Token;
import splat.parser.elements.ProgramAST;

public class ParserTester {

	private File testDir;

	private boolean verbose;
	private int testCount;
	private int success;
	private int failure;

	public static void main(String[] args) {

		ParserTester tester = new ParserTester(true);
		tester.runTests();
	}

	public ParserTester(boolean seeResults) {
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
			// All of our parser testing files should pass lexing
			if (testFile.getName().endsWith("_badparse.splat") 
					|| testFile.getName().endsWith("_goodparse.splat")) {
				try {
					runTest(testFile);
				} catch (IOException e) {
					// This really shouldn't happen if we've already entered
					// into the test directory
					e.printStackTrace();
				} catch (LexException e) {
					// All of our goodparse and badparse files should pass lexing
					e.printStackTrace();
				}
			}
		}

		// Finally, we output the results

		System.out.println("----------------------------");
		System.out.println("FINAL PARSER TESTING RESULTS");
		System.out.println("----------------------------");
		System.out.println("Tests completed: " + testCount);
		System.out.println("Tests succeeded: " + success);
		System.out.println("Tests falied:    " + failure);
	}

	private void runTest(File testFile) throws IOException, LexException {

		Lexer lexer = new Lexer(testFile);
		List<Token> tokenz = lexer.tokenize();
		Parser parser = new Parser(tokenz);
		
		
		boolean expectParseError = testFile.getName().endsWith("_badparse.splat");
		
		try {

			testCount++;
			System.out.print("Test Case " + testCount + ": " + testFile.getName() + "...");

			ProgramAST prog = parser.parse();

			// If we successfully get to here, no parse exceptions were thrown

			// If we expected an error, but an exception wasn't thrown...fail
			if (expectParseError) {

				failure++;
				System.out.println("failed - parse error should have been detected");
				
				System.out.println(prog);
				
			// No exceptions thrown with no lex errors in the file...success
			} else {
				
				success++;
				System.out.println("passed (parsing successful)");
				
				if (verbose) {
					System.out.println(prog);
				}
			}

		} catch (ParseException ex) {

			// If a parse error is what was expected...success
			if (expectParseError) {
				success++;
				System.out.println("passed (parse error caught)");
				if (verbose) {
					System.out.println(ex.getMessage());
					System.out.println();
				}

			// If an exception was thrown, but it shouldn't have been...fail
			} else {
				failure++;
				System.out.println("failed - parsing should have been successful");
				System.out.println(ex.getMessage());
				System.out.println();
			}

		}
	}
	
}
