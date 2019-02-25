package j2script;

import j2script.tokens.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Tokenizer {
    private final char[] input;
    private int inputPos;

    private static Map<String, Token> TOKEN_MAPPING =
        new HashMap<String, Token>() {{
            put("+", new AddToken());
            put("boolean", new BooleanToken());
            put("break", new BreakToken());
            put("class", new ClassToken());
            put("/", new DivToken());
            put("else", new ElseToken());
            put("=", new EqualToken());
            put("extends", new ExtendsToken());
            put("if", new IfToken());
            put("int", new IntToken());
            put("{", new LeftCurlyToken());
            put("(", new LeftParenToken());
            put("*", new MultiplyToken());
            put("new", new NewToken());
            put("println", new PrintToken());
            put("private", new PrivateToken());
            put("public", new PublicToken());
            put("return", new ReturnToken());
            put("}", new RightCurlyToken());
            put(")", new RightParenToken());
            put(";", new SemiToken());
            put("string", new StringToken());
            put("-", new SubtractToken());
            put("this", new ThisToken());
            put("void", new VoidToken());
            put("while", new WhileToken());
        }
    };
    public Tokenizer(final char[] input) {
        this.input = input;
        inputPos = 0;
    }

    private void skipWhitespace() {
        //Advances input position past whitespace
        while (inputPos < input.length &&
                Character.isWhitespace(input[inputPos])) {
            inputPos++;
        }
    }

    private Token tryTokenizeOther() {
        for (final Map.Entry<String, Token> entry : TOKEN_MAPPING.entrySet()) {
            final String key = entry.getKey();
            //check if the input is equal to any key.
            if (prefixCharsEqual(key)) {
                inputPos += key.length();               
                return entry.getValue();
            }
        }
        return null;
    }

    private boolean prefixCharsEqual(final String probe) {
        //Get the current input index.
        int targetPos = inputPos;
        //Start index of key at 0
        int probePos = 0;
        //Check if the current input is equal to this key (probe)
        while (targetPos < input.length &&
                probePos < probe.length() &&
                probe.charAt(probePos) == input[targetPos]) {
            probePos++;
            targetPos++;
        }
        //return whether or not there is a prefix match.
        return probePos == probe.length();
        /*(!) Note: Would fail if we had a keyword that was 
        a prefix of another keyword, i.e. int int64
        */
    }

    private NumberToken tryTokenizeNumber() {
        final int initialInputPos = inputPos;
        String digits = "";
 
        while (inputPos < input.length &&
               Character.isDigit(input[inputPos])) {
            digits += input[inputPos];
            inputPos++;
        }
 
        if (digits.length() > 0) {
            return new NumberToken(Integer.parseInt(digits));
        } else {
            // reset position
            inputPos = initialInputPos;
            return null;
        }
    }

    private VariableToken tryTokenizeVariable() {
        final int initialInputPos = inputPos;
        String name = "";
        
        //The first character must be a letter.
        if (Character.isLetter(input[inputPos])) {
            name += input[inputPos];
            inputPos++;
            //The rest of the characters are letters or digits. No underscores allowed.
            while (inputPos < input.length &&
                    Character.isLetterOrDigit(input[inputPos])) {
                name += input[inputPos];
                inputPos++;
            }
        } else {
            // reset position
            inputPos = initialInputPos;
            return null;
        }
        
        //Check if the string is actually a reserved keyword.
        if (isTokenString(name)) {
            // reset position
            inputPos = initialInputPos;
            return null;
        } else {
            return new VariableToken(name);
        }
    }
    
    //(!) Carlos will finish.
    //Checks to see if next token is string
    //(!) Needs double quote token
//     
//     private StringToken tryTokenizeString()
//     {
//         final int startingInputPos = this.inputPos;
//         boolean validEscapeChar = false;
//         String stringToken = "";
//         
//         //Look for the 1st double quote
//         if(this.input[this.inputPos] == "\"")
//         {
//             //pass the 1st double quote
//             this.inputPos++;
//             
//             //Process until another double quote as part of the string.
//             while(this.inputPos < this.input.length && 
//                     this.input[this.inputPos] != "\"")
//             {
//             
//                 //Because escapes are treated as single characters
//                 // we look for those first.
//                 if(this.inputPos < this.input.length())
//                 {
//                     //Check if escape char
//                     /*(!)Note: If we're testing w/ strings, 
//                      _Java compiler won't allow illegal escape characters.
//                     */
//                     switch(this.input[this.inputPos])
//                     {  
//                         case '\n': //newline                            
//                         case '\\': //backslash literal
//                         case '\"': //double quote literal
//                         case '\'': //single quote literal (not necessary ? since this is looking for string)
//                         case '\t': //tab
//                             validEscapeChar = true;
//                             break;                   
//                         default:                            
//                             break;
//                     }//end switch check for escape
//                 }//end input bounds check
//                 
//                 /*  (!)Implementation when reading code from a file,
//                     instead of a Java string in our own code.
//                     If we read chars as a file, 
//                     then each char will translate to Java char:
//                     "\x"->"\"\\x\"" (first and last quote signify enclosed string in Java.)
//                     If we tried passing this string in Java using a JUnit test method,
//                     The compiler will not even allow this:
//                     error: illegal escape character
//                 */
//                 /*                                  
//                 //Check if it's an escape character. (!)How to handle these.
//                 //Check if this char is a backslash to introduce an escape char
//                 if(this.input[this.inputPos] == "\\" &&
//                     this.inputPos+1 < this.input.length)
//                 {
//                     //check if the following char makes a valid escape char.
//                     switch(this.input[this.inputPos])
//                     {
//                         case 'n': //newline
//                             stringToken += "\n";
//                         case 'r': //return carriage
//                             stringToken += "\r";
//                         default:
//                             break;
//                     }        
//                 }
//                 else //not an escape char
//                 {
//                     //Accumulate the characters
//                     stringToken += this.input[this.inputPos];
//                     this.inputPos++;
//                 }                
//                 */
//                 
//                 //if found a valid escape char, 
//                 //_add it to accumulation
//                 if(validEscapeChar)
//                 {
//                     validEscapeChar = false;
//                     stringToken += this.input[this.inputPos];
//                     this.inputPos++;
//                 }
//                 else //Check if it's some other type of char.
//                 {
//                     //(!) Should return null when there is an illegal escape char,
//                     //_but Java compiler will handle that.
//                     
//                     
//                 }    
//                     
//                 
//               
//             }//End while checking for 2nd double quote.
//             
//             //Check if full valid string was 
//             //_found by checking last char found.
//             if(this.input[this.inputPos] == "\"")
//             {
//                 this.inputPos++;
//                 return new StringToken(stringToken);
//             }
//             
//         }//First char wasn't a double quote
//         this.inputPos = startingInputPos;
//         return null;        
//     }//end tryTokenizeString()
    
    
    // returns null if there are no more tokens
    public Token tokenizeSingle() throws TokenizerException {
        VariableToken var = null;
        NumberToken num = null;
        Token otherToken = null;

        skipWhitespace();

        if (inputPos >= input.length) {
            return null;
        } else if ((var = tryTokenizeVariable()) != null) {
            return var;
        } else if ((num = tryTokenizeNumber()) != null) {
            return num;
        } else if ((otherToken = tryTokenizeOther()) != null) {
            return otherToken;
        } else {
            throw new TokenizerException("Invalid character " +
                    input[inputPos] +
                    " at position " +
                    inputPos);
        }
    }

    public static boolean isTokenString(final String input) {
        
        return TOKEN_MAPPING.containsKey(input);
    }

    public List<Token> tokenize() throws TokenizerException {
        List<Token> list = new ArrayList<Token>();
        Token current = null;

        while ((current = tokenizeSingle()) != null) {
            list.add(current);
        }

        return list;
    }
}