package splat.executor;

public class MIPSCode {

    private StringBuilder programCode;
    private StringBuilder programData;

    public MIPSCode() {
        programCode = new StringBuilder();
        programCode.append(".text\n");
        programCode.append("############ main program ############\n");
        programCode.append("main: move $fp $sp\n");

        programData = new StringBuilder();
        programData.append("############ data section (for strings) ############\n");
        programData.append(".data\n");
        programData.append("new_line_string: .asciiz \"\\n\"\n");
        programData.append("false_string: .asciiz \"false\"\n");
        programData.append("true_string: .asciiz \"true\"\n");
    }

    public void append(String code) {
        programCode.append(code);
    }

    public void appendToData(String data) {
        programData.append(data);
    }

    public void addNewStringLiteral(String newLabel, String strLit) {
        programData.append(newLabel+": .asciiz " + strLit + "\n");
    }

    public String toString() {
        return programData.toString() + programCode.toString();
    }
}
