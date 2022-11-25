package splat.lexer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

	private final File progfile;
	private final List<Token> tokens;

	public Lexer(File progFile) {
		this.progfile = progFile;
		tokens = new ArrayList<>();
		// TODO Auto-generated constructor stub
	}

	public List<Token> tokenize() throws LexException {


		String token_value;

		String line_of_file;
		int line = 1 , column;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(progfile));
			while ((line_of_file = reader.readLine()) != null) {
				column = 0;
				token_value = "";
				while(line_of_file.length() > column){

					if (line_of_file.charAt(column) == ' ' || line_of_file.charAt(column) == '\n'|| line_of_file.charAt(column) == '\r' || line_of_file.charAt(column) == '\t') {
						if (token_value.length() > 0) {
							tokens.add(new Token(token_value,column,line));
							token_value = "";
						}
						column++;
					}
					else if (Character.isDigit(line_of_file.charAt(column))) {

						token_value += line_of_file.charAt(column);
						column++;
					}
					else if (Character.isLetter(line_of_file.charAt(column)) || line_of_file.charAt(column) == '_') {
						if (token_value.length() >0 && Character.isDigit(token_value.charAt(0))){
							throw new LexException("Incorrect variable",line,column+1); // with 123name like
						}

						token_value += line_of_file.charAt(column);
						column++;
					}
					else if (line_of_file.charAt(column) == '"') {
						if(token_value.length() > 0){
							tokens.add(new Token(token_value,column,line));
							token_value = "";
						}


						while (true) {
							token_value += line_of_file.charAt(column);
							column++;

							if (line_of_file.charAt(column) == '"') {
								token_value += line_of_file.charAt(column);
								break;
							}

							else if (column == line_of_file.length()-1 && line_of_file.charAt(column) != '"') {
								throw new LexException("Reached end of string and no quote", line, column+1);
							}

						}
						tokens.add(new Token(token_value,column,line));
						column++;
						token_value = "";
					}
					else{
						final String[] VALID_OPERATORS = {"==", ">=", "<=",":=", "+", "-","*","%", "(", ")","{","}","[","]", ":", ";",".",",",">","<","&","#","/"};
						boolean foundOperator = false;

						for (String operator : VALID_OPERATORS) {
							if (line_of_file.substring(column).startsWith(operator)) {
								if (token_value.length() > 0) {
									tokens.add(new Token(token_value,column,line));
									token_value = "";
								}
								token_value += operator;
								tokens.add(new Token(token_value,column,line));
								token_value = "";
								column += operator.length();
								foundOperator = true;
								break;
							}
						}

						if(!foundOperator){
							throw new LexException("Invalid Operator",line,column+1);
						}

					}
				}

				if (token_value.length() > 0) {
					tokens.add(new Token(token_value,column,line));
					token_value = "";
				}

				line++;
			}
			reader.close();

		}

		catch (IOException e) {
			throw new LexException("Something wrong with File",-1,-1);
		}

		// TODO Auto-generated method stub
		return tokens;
	}

}