package splat.parser;

import splat.lexer.LexException;
import splat.lexer.Lexer;
import splat.lexer.Token;
import splat.parser.elements.ProgramAST;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParseException, LexException {
        File file = new File("tests/013_01_goodexecution.splat");
        Lexer lexer = new Lexer(file);
        List<Token> tokenz = lexer.tokenize();
        Parser parser = new Parser(tokenz);

        ProgramAST prog = parser.parse();
        System.out.println(prog);
    }
}
