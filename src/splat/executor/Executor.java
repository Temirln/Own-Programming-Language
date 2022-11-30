package splat.executor;

import splat.Splat;
import splat.SplatException;
import splat.parser.elements.*;
import splat.parser.elements.DECL.FunctionDecl;
import splat.parser.elements.DECL.VariableDecl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Executor {

	private ProgramAST progAST;
	private MIPSCode mipsCode;
	private File mipsFile;
	
	public Executor(ProgramAST progAST) {
		this.progAST = progAST;
	}

	public void runProgram() throws ExecutionException {

		// Convert the AST into a MIPS program
		convertASTtoMIPS();
//		System.out.println("\nconverted\n");

//		mipsFile = new File("MipsProg.mips");
		// Save the program to a file
		try {
			saveMIPSCodeToFile();
		} catch (IOException e) {
			e.printStackTrace();
			throw new ExecutionException("Internal Error - problem saving MIPS program to file");
		}

		// Run the MIPS program
		int exitCode = MIPSRunner.runMIPSProg(mipsFile);
//		System.out.println(MIPSRunner);
		// Take care of any errors
		if (exitCode == -1) {
			throw new ExecutionException("Divide by zero error.");
		} else if (exitCode == -2) {
			throw new ExecutionException("Program timeout.");
		} else if (exitCode == -3) {
			throw new ExecutionException("Internal error.");
		} else if (exitCode != 0) {
			throw new ExecutionException("Unknown exit code " + exitCode);
		}
	}

	private void convertASTtoMIPS() throws ExecutionException {

		mipsCode = convertToMIPS(progAST);

	}

	private void saveMIPSCodeToFile() throws IOException {

		mipsFile = new File("MipsProg.mips");
		if (mipsFile.exists()) {
			mipsFile.delete();
		}
		mipsFile.createNewFile();

		BufferedWriter writer = new BufferedWriter(new FileWriter(mipsFile));
		writer.write(mipsCode.toString());
		writer.flush();
		writer.close();
	}

	private MIPSCode convertToMIPS(ProgramAST progAST) {

		MIPSCode mipsCode = new MIPSCode();

		// Extract the variables for the main program
		List<VariableDecl> varDecls = new ArrayList<>();

		for (Declaration decl : progAST.getDecls()) {
			if (decl instanceof VariableDecl) {

				VariableDecl vdecl = (VariableDecl)decl;
				varDecls.add(vdecl);

			}
		}


//		System.out.println(varDecls);

		// Create a new frame info object for the "main" function
		StackFrameInfo frameInfo = new StackFrameInfo("main", varDecls);

//		System.out.println(frameInfo.numberOfVars());

		// Convert the main program body to code
		for (Statement stmt : progAST.getStmts()) {
			stmt.convertToMIPS(mipsCode, frameInfo);
		}

		// This is the end of the program
		mipsCode.append("li $v0 10\n");
		mipsCode.append("syscall  #terminating program normally\n");

		// This is the error reporting end of the program
		mipsCode.append("fail_and_exit: li $a0 -1\n");
		mipsCode.append("li $v0 17\n");
		mipsCode.append("syscall  #terminating program with error\n");

		// Add the function definition blocks at the very end
		for (Declaration decl : progAST.getDecls()) {
			if (decl instanceof FunctionDecl) {
				FunctionDecl fdecl = (FunctionDecl) decl;
				fdecl.convertToMIPS(mipsCode);
			}
		}

		System.out.println(mipsCode+"\n");
		return mipsCode;
	}

	// Convenience method that you can use to try to convert and run a
	// single test case
	public static void main(String[] args) throws SplatException {

		Splat splat = new Splat(new File(new File("tests"),
				"777_01_goodexecution.splat"));

		splat.processFileAndExecute();
	}
}
