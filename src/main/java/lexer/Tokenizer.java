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
        new HashMap<String, Token>() {
        {
            put("+", new AddToken());
            put("boolean", new BooleanToken());
            put("==", new BooleanEqualsToken());
            put("break", new BreakToken());
            put("class", new ClassToken());
            put(",", new CommaToken());
            put("constructor", new ConstructorToken());
            put("/", new DivToken());
            put("else", new ElseToken());
            put("=", new EqualToken());
            put("extends", new ExtendsToken());
            put("false", new FalseToken());
            put("if", new IfToken());
            put("int", new IntToken());
            put("{", new LeftCurlyToken());
            put("(", new LeftParenToken());
            put("-", new MinusToken());
            put("*", new MultiplyToken());
            put("new", new NewToken());
            put("println", new PrintToken());
            put("private", new PrivateToken());
            put("public", new PublicToken());
            put("return", new ReturnToken());
            put("}", new RightCurlyToken());
            put(")", new RightParenToken());
            put(";", new SemiToken());
            put("super", new SuperToken());
            put("true", new TrueToken());
            put("void", new VoidToken());
            put("while", new WhileToken());
            put("<", new LessThanToken());
            put(">", new GreaterThanToken());
        }
    };
    public Tokenizer(final char[] input) {
        this.input = input;
        inputPos = 0;
    }

    private void skipWhitespace() {
        //Advances input position past whitespace
        while (inputPos < input.length &&
              (Character.isWhitespace(input[inputPos]))) {
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
        return new VariableToken(name);
    }    
    
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

    public List<Token> tokenize() throws TokenizerException {
        List<Token> list = new ArrayList<Token>();
        Token current = null;

        while ((current = tokenizeSingle()) != null) {
            list.add(current);
        }

        return list;
    }
}