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

    public static void main(String[] args) throws FileNotFoundException, ParserException, TokenizerException,
                                                  TypeErrorException, IOException {
        File inputFile = new File("./testPrograms/" + args[0]);
        // lexer reads file in as char[] returns arraylist of tokens
        Tokenizer tokenizer = new Tokenizer(readFile(inputFile));
        final List<Token> tokens = tokenizer.tokenize();
        // create parser with tokens as input then call parseMe returns a Program
        Parser parser = new Parser(tokens);
        final Program program = parser.parseMe();
        // final Program program = null;
        // call parse program with Program object
        TypeChecker.typecheckProgram(program);
        // Create new file object for output
        File outputFile = new File(inputFile.getName() + ".js");
        // create Codegen object
        Codegen codeGen = new Codegen();
        // call writeProgramtoFile on codegen with Program object and output file
        codeGen.writeProgramtoFile(program, outputFile);
    }
}