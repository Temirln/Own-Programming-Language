package splat.semanticanalyzer;

import splat.lexer.LexException;
import splat.lexer.Lexer;
import splat.lexer.Token;
import splat.parser.ParseException;
import splat.parser.Parser;
import splat.parser.elements.ProgramAST;

import java.io.File;
import java.util.List;

public class Main {
    public static void main (String[] args) throws LexException, ParseException, SemanticAnalysisException {
        System.out.println("Main");
        File file = new File("tests/gk_01_badsemantics.splat");
        Lexer lexer = new Lexer(file);
        List<Token> tokenz = lexer.tokenize();
        Parser parser = new Parser(tokenz);
        ProgramAST prog = parser.parse();

        System.out.println(prog);
        SemanticAnalyzer analyzer = new SemanticAnalyzer(prog);
        analyzer.analyze();

//        out:
//        for (int i=0 ;i<10;i++){
//            for (int j = i;j<11;j++){
//                if (j==5){
//                    break out;
//                }
//                System.out.println("heyhey");
//            }
//            System.out.println("hey");
//        }

    }
}
