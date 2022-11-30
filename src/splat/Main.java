package splat;

import java.io.File;

public class Main {
    public static void main(String[] args) throws SplatException {
        System.out.println("Main");
        File file = new File("tests/007_02_goodexecution.splat");
        Splat splat = new Splat(file);
        splat.processFileAndExecute();
    }
}
