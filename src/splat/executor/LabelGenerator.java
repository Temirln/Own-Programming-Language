package splat.executor;

public class LabelGenerator {

    private static int whileLabelNum = 0;
    private static int ifLabelNum = 0;
    private static int stringLabelNum = 0;
    private static int generalLabelNum = 0;

    public static String getNewWhileLabel() {
        whileLabelNum++;
        return "while_label_"+whileLabelNum;
    }

    public static String getNewIfLabel() {
        ifLabelNum++;
        return "if_label_"+ifLabelNum;
    }

    public static String getNewStringLabel() {
        stringLabelNum++;
        return "string_lit_label_"+stringLabelNum;
    }

    public static String getNewGeneralLabel() {
        generalLabelNum++;
        return "general_label_"+generalLabelNum;
    }
}
