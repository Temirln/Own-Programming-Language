package splat.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class MIPSRunner {

    private static String marsJarLoc = "libs\\Mars45.jar";

    public static int runMIPSProg(File mipsFile) {

        String filepath = mipsFile.getPath();
        String[] arr = {filepath, "nc"};

        String cmd = "java -jar " + marsJarLoc + " \"" + filepath + "\" nc";

        int exitCode = 0;
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            printProgramOutput(process);

            if (!process.waitFor(5, TimeUnit.SECONDS)) {
                System.out.println("Timeout");
                process.destroy();
                exitCode = -2;
            }

            if (process.exitValue() != 0) {
                exitCode = process.exitValue();
            }

            // printProgramOutput(process);

        } catch (Exception e) {
            e.printStackTrace();
            exitCode = -3;
        }

        return exitCode;
    }

    private static void printProgramOutput(Process process) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        StringBuffer buf = new StringBuffer();
        int ch = reader.read();
        int ch2 = reader.read();

        while (ch2 != -1) {

            buf.append((char)ch);
            ch = ch2;
            ch2 = reader.read();
        }

        System.out.print(buf);
    }
}
