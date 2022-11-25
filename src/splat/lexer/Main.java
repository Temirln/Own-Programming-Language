package splat.lexer;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, LexException {


        String token_value = "";
        File file = new File("tests/bt_00a_badparse.splat");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line_of_file;
        int line = 1 , column;
        try {
            while ((line_of_file = reader.readLine()) != null) {
                column = 0;
                token_value = "";
                while(line_of_file.length() > column){

                    if (line_of_file.charAt(column) == ' ' || line_of_file.charAt(column) == '\n'|| line_of_file.charAt(column) == '\r' || line_of_file.charAt(column) == '\t') {
                        if (token_value.length() > 0) {
                            System.out.println("Token _ " + token_value);
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
                            System.out.println("Token _ " + token_value);
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

                        System.out.println("Token + " + token_value);
                        column++;
                        token_value = "";
                    }
                    else{
                        final String[] VALID_OPERATORS = {"==", ">=", "<=",":=", "+", "-","*","%", "(", ")","{","}","[","]", ":", ";",".",",",">","<","&","#"};
                        boolean foundOperator = false;

                        for (String operator : VALID_OPERATORS) {
                            if (line_of_file.substring(column).startsWith(operator)) {
                                if (token_value.length() > 0) {
                                    System.out.println("Token _ " + token_value);
                                    token_value = "";
                                }
                                token_value += operator;
                                System.out.println("Token _ " + token_value);
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
                    System.out.println("Token _ " + token_value);
                    token_value = "";
                }
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        int ch;
//        String line;
//        String token = "";
//        while ((line = reader.read()) != -1) {
//
//                System.out.println((char)ch);
//                if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == ')' || ch == '(' || ch == ':'){
//                    if(token.length() > 0){
//                        System.out.println("Tokens: "+token);
//                        token = "";
//                    }
//
//                }
//                else{
//                    token = token + (char)ch;
//                }
//
////
//        }
//
//        reader.close();

//        String line;





//        while ((line = reader.readLine()) != null) {
//            if (line == null){
//                break;
//            }





//            Deque<Character> stack = new ArrayDeque<Character>();
//
//            // Traversing the Expression
//            for (int i = 0; i < line.length(); i++) {
//                char x = line.charAt(i);
//
//                if (x == '(' || x == '[' || x == '{') {
//                    // Push the element in the stack
//                    stack.push(x);
//                    continue;
//                }
//
//                // If current character is not opening
//                // bracket, then it must be closing. So stack
//                // cannot be empty at this point.
////                if (stack.isEmpty()){
////                    throw new LexException("Invalid Bracket",row,column);
////                }
//
//                char check;
//                switch (x) {
//                    case ')':
//                        check = stack.pop();
//                        if (check == '{' || check == '[') {
//                            throw new LexException("Invalid Bracket",row,column);
//                        }
//                        break;
//
//                    case '}':
//                        check = stack.pop();
//                        if (check == '(' || check == '['){
//                            throw new LexException("Invalid Bracket",row,column);
//                        }
//                        break;
//
//                    case ']':
//                        check = stack.pop();
//                        if (check == '(' || check == '{'){
//                            throw new LexException("Invalid Bracket",row,column);
//                        }
//                        break;
//                }
//            }
//            int ch;
//            int row = 0;
//            while ((ch = reader.read()) != -1) {
//                if (ch == '\n'){
//                    row = 0;
//                    column++;
//                }
////                int x = OPERATOR_CHARS.indexOf(line.charAt(row));
////                System.out.println(x);
//                if (ch == ' ' || ch == '\r' || ch == '\t') {
//                    if (token_value.length() > 0) {
//                        System.out.println("Token _ " + token_value);
//                        token_value = "";
//                    }
//                    row++;
//                }
//                else if(Character.isWhitespace(ch)){
//                    if (token_value.length() > 0) {
//                        System.out.println("Token _ " + token_value);
//                        token_value = "";
//                    }
//                }
//                else if (Character.isDigit(ch)) {
//                    token_value += (char)ch;
//                    row++;
//                }
//                else if (Character.isLetter(ch) || ch == '_') {
//                    token_value += (char)ch;
//                    row++;
//                }
//                else if (ch == '"') {
//                    int row_string = row;
//                    int column_string = column;
////                    while()
//                    if(token_value.length() > 0){
//                        System.out.println("Token _ " + token_value);
//                        token_value = "";
//                    }
//
//                    token_value += (char)ch;
//                    System.out.println("Token _ " + token_value);
//                    token_value = "";
//                } else{
//
//                    boolean foundOperator = false;
//                    List vowelsList = Arrays.asList(VALID_OPERATORS);
//                    int i = vowelsList.indexOf(String.valueOf((char)ch));
//                    if(vowelsList.contains(String.valueOf((char)ch))){
//                        if (token_value.length() > 0) {
//                            System.out.println("Token _ " + token_value);
//                            token_value = "";
//                        }
//                        token_value += VALID_OPERATORS[i];
//                        System.out.println("Token _ "+token_value);
//                        token_value = "";
////                        System.out.println("Token: "+token_value);
////                        System.out.println(vowelsList.get(i));
//                        row += VALID_OPERATORS[i].length();
//                        foundOperator = true;
//                    }
//                    if(!foundOperator){
//                        throw new LexException("Invalid Operator",row,column);
//                    }
////                    for (String operator : VALID_OPERATORS) {
////                        if (ch.startsWith(operator)) {
////                            System.out.println("Token_before: " + token_value);
////                            token_value = "";
////                            token_value += operator;
////                            System.out.println("Token: " + token_value);
////                            token_value = "";
////                            System.out.println(operator.length());
////                            row += operator.length();
////                            break;
////                        }
////                    }
//                    row++;
//
//                }
////                column++;
////                System.out.println();
//            }
//            reader.close();
        }
    }

