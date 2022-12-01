package splat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import splat.Splat;
import splat.executor.ExecutionException;
import splat.lexer.LexException;
import splat.parser.ParseException;
import splat.semanticanalyzer.SemanticAnalysisException;

public class SplatTester {

	private File testDir;

	private boolean verbose;
	private int testCount;
	private int success;
	private int failure;

	public static void main(String[] args) {

		SplatTester tester = new SplatTester(true);
		tester.runTests();
	}

	public SplatTester(boolean seeResults) {
		verbose = seeResults;
		testCount = 0;
		success = 0;
		failure = 0;
	}

	public void runTests() {

		// First, we get the testing files

		testDir = new File("tests2");

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
			// Make sure we only test .splat files
			if (testFile.getName().endsWith(".splat")) {
				try {
					runTest(testFile);
				} catch (Exception e) {
					// This really shouldn't happen
					e.printStackTrace();
				}
			}
		}

		// Finally, we output the results

		System.out.println("---------------------------");
		System.out.println("FINAL SPLAT TESTING RESULTS");
		System.out.println("---------------------------");
		System.out.println("Tests completed: " + testCount);
		System.out.println("Tests succeeded: " + success);
		System.out.println("Tests falied:    " + failure);
	}

	private void runTest(File testFile) throws IOException {

		Splat splat = new Splat(testFile);

		// Used to redirect output to a temp file
		PrintStream outs = null;

		boolean expectError = !testFile.getName().endsWith("_goodexecution.splat");

		try {

			testCount++;
			PrintStream original = new PrintStream(System.out);
			System.out.print("\nTest Case " + testCount + ": " + testFile.getName() + "...");
			File progOutput = new File(testDir, "temp-out.txt");
			outs = new PrintStream(progOutput);
			if (!expectError) {
				// Captures the program output to a file

				System.setOut(outs);

				// Run the analyzer on the program AST
				splat.processFileAndExecute();

				// Returns output to the console window
				outs.close();
				System.setOut(original);
			} else {
				splat.processFileAndExecute();
			}
			
			///////////////
			// If we successfully get to here, no exceptions were thrown
			//////////////
			
			// Get the .txt file with the expected output
			String testFilePath = testFile.getAbsolutePath();
			String exFilename = testFilePath.substring(0, testFilePath.length() - 5) + "out";
			File expectedOutput = new File(exFilename);

			// If we expected an error, but an exception wasn't thrown...fail
			if (expectError) {

				failure++;
				System.out.println("failed - error should have been detected");

				printOutput(progOutput);

				// No exceptions thrown -- now check the output
			} else if (outputMatchesExpected(progOutput, expectedOutput)) {

				success++;
				System.out.println("passed (output matches expected results)");

				if (verbose) {
					printOutput(progOutput);
				}
			} else {

				failure++;
				System.out.println("failed (output does not match expected results)");

				if (verbose) {
					printOutput(progOutput);
				}
			}

		} catch (SplatException ex) {

			// Returns output to the console window
			outs.close();
			System.setOut(System.out);

			// If we got the expected error...success
			if (exceptionMatchesFilename(ex, testFile)) {

				success++;
				System.out.println("passed (proper error type caught)");
				if (verbose) {
					System.out.println(ex.getMessage());
					System.out.println();
				}

				// If an exception was thrown, but it shouldn't have been...fail
			} else if (!expectError) {

				failure++;
				System.out.println("failed - error detected when program " + "should execute successfully");
				System.out.println(ex.getMessage());
				System.out.println();

			} else {
				failure++;
				System.out.println("failed - wrong error type caught");
				System.out.println("(Actual exception type: " + ex.getClass().toString() + ")");
				System.out.println(ex.getMessage());
				System.out.println();
			}

		}

	}

	private boolean outputMatchesExpected(File output, File expected) throws IOException {

		if (!expected.exists()) {
			System.out.println("File " + expected.getAbsolutePath() + " not found");
			return false;
		}

		BufferedReader readerOut = new BufferedReader(new FileReader(output));
		BufferedReader readerEx = new BufferedReader(new FileReader(expected));

		boolean result = true;

		int chOut = readerOut.read();
		int chEx = readerEx.read();

		while (true) {

			while (chOut == '\r') {
				chOut = readerOut.read();
			}
			while (chEx == '\r') {
				chEx = readerEx.read();
			}

			if (chOut == -1 && chEx == -1) {

				result = true;
				break;

			} else if (chOut != chEx) {
				result = false;
				break;
			}

			chOut = readerOut.read();
			chEx = readerEx.read();
		}

		readerOut.close();
		readerEx.close();

		return result;
	}

	private void printOutput(File file) throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(file));

		int ch = reader.read();

		while (ch != -1) {
			System.out.print((char)ch);
			ch = reader.read();
		}

		reader.close();

	}

	private boolean exceptionMatchesFilename(SplatException ex, File file) {

		String filename = file.getName();

		if (ex instanceof LexException) {
			return filename.endsWith("_badlex.splat");
		} else if (ex instanceof ParseException) {
			return filename.endsWith("_badparse.splat");
		} else if (ex instanceof SemanticAnalysisException) {
			return filename.endsWith("_badsemantics.splat");
		} else if (ex instanceof ExecutionException) {
			return filename.endsWith("_badexecution.splat");
		}

		// We will never get here
		return false;
	}
}
