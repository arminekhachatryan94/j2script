package j2script;

import j2script.tokens.Token;
import j2script.TokenizerException;
import j2script.ParserException;
import j2script.TypeErrorException;
import j2script.declarations.Program;
import j2script.Parser;
import j2script.TypeChecker;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;

public class main {
    private static char[] readFile(File f) throws FileNotFoundException {
        String theString = "";
        Scanner scanner = new Scanner(f);

        theString = scanner.nextLine();
        while (scanner.hasNextLine()) {
            theString = theString + "\n" + scanner.nextLine();
        }
        char[] charArray = theString.toCharArray();
        return charArray;
    }

    private static void createHTML(File dir) throws IOException{
        File f = new File(dir, "index.html");
        if(!f.exists() && !f.isDirectory()) { 
            System.out.println("Vreating HTML");
            final PrintWriter output= new PrintWriter(new BufferedWriter(new FileWriter(f)));
            try{
                output.println("<!DOCTYPE html>");
                output.println("<html lang=\"en\">");
                output.println("<head>");
                output.println("    <meta charset=\"UTF-8\">");
                output.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
                output.println("    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">");
                output.println("    <title>j2Script</title>");
                output.println("</head>");
                output.println("<body>");
                output.println("    ");
                output.println("</body>");
                output.println("<script src=\"Test.js\"></script>");
                output.println("</html>");
            }finally{
                output.close();
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException, ParserException, TokenizerException,
                                                  TypeErrorException, IOException {
        File inputFile = new File(args[0]);
        // lexer reads file in as char[] returns arraylist of tokens
        Tokenizer tokenizer = new Tokenizer(readFile(inputFile));
        final List<Token> tokens = tokenizer.tokenize();
        // create parser with tokens as input then call parseMe returns a Program
        Parser parser = new Parser(tokens);
        final Program program = parser.parseMe();
        // call parse program with Program object
        TypeChecker.typecheckProgram(program);
        // Create new file object for output
        File dir = new File ("output/");
        File outputFile = new File(dir, inputFile.getName().substring(0, (inputFile.getName()).length() - 4) + ".js");
        // create Codegen object
        Codegen codeGen = new Codegen();
        // call writeProgramtoFile on codegen with Program object and output file
        codeGen.writeProgramtoFile(program, outputFile);
        createHTML(dir);
    }
}