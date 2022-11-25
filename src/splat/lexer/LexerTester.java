package splat.lexer;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LexerTester {

	private File testDir;

	private boolean verbose;
	private int testCount;
	private int success;
	private int failure;

	public static void main(String[] args) {

		LexerTester tester = new LexerTester(true);
		tester.runTests();
	}

	public LexerTester(boolean seeResults) {
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
			if (testFile.getName().endsWith("_badlex.splat") 
			 || testFile.getName().endsWith("_goodlex.splat")) {
				try {
					runTest(testFile);
				} catch (IOException e) {
					// This really shouldn't happen if we've already entered
					// into the test directory
					e.printStackTrace();
				}
			}
		}

		// Finally, we output the results

		System.out.println("---------------------------");
		System.out.println("FINAL LEXER TESTING RESULTS");
		System.out.println("---------------------------");
		System.out.println("Tests completed: " + testCount);
		System.out.println("Tests succeeded: " + success);
		System.out.println("Tests falied:    " + failure);
	}

	private void runTest(File testFile) throws IOException {

		Lexer lexer = new Lexer(testFile);

		boolean expectLexError = testFile.getName().endsWith("_badlex.splat");
		
		try {

			testCount++;
			System.out.print("Test Case " + testCount + ": " + testFile.getName() + "...");

			List<Token> tokens = lexer.tokenize();

			// If we successfully get to here, no lex exceptions were thrown

			// If we expected an error, but an exception wasn't thrown...fail
			if (expectLexError) {

				failure++;
				System.out.println("failed - lex error should have been detected");
				
				outputTokens(tokens);
				
			// No exceptions thrown with no lex errors in the file...success
			} else {
				
				success++;
				System.out.println("passed (lexing successful)");
				
				if (verbose) {
					outputTokens(tokens);
				}
			}

		} catch (LexException ex) {

			// If a lex error is what was expected...success
			if (expectLexError) {
				success++;
				System.out.println("passed (lex error caught)");
				if (verbose) {
					System.out.println(ex.getMessage());
					System.out.println();
				}

			// If an exception was thrown, but it shouldn't have been...fail
			} else {
				failure++;
				System.out.println("failed - lexing should have been successful");
				System.out.println(ex.getMessage());
				System.out.println();
			}

		}
	}

	public void outputTokens(List<Token> tokens) {
		System.out.println("TOKENS:");
		
		if (tokens == null) {
			System.out.println("Token list should not be null");
			return;
		}
		for (Token token : tokens) {
			System.out.println(token);
		}
	}
}
