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
            put("Boolean", new BooleanToken());
            put("Break", new BreakToken());
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
        while (inputPos < input.length &&
                Character.isWhitespace(input[inputPos])) {
            inputPos++;
        }
    }

    private Token tryTokenizeOther() {
        for (final Map.Entry<String, Token> entry : TOKEN_MAPPING.entrySet()) {
            final String key = entry.getKey();
            if (prefixCharsEqual(key)) {
                inputPos += key.length();
                return entry.getValue();
            }
        }
        return null;
    }

    private boolean prefixCharsEqual(final String probe) {
        int targetPos = inputPos;
        int probePos = 0;

        while (targetPos < input.length &&
                probePos < probe.length() &&
                probe.charAt(probePos) == input[targetPos]) {
            probePos++;
            targetPos++;
        }

        return probePos == probe.length();
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

        if (Character.isLetter(input[inputPos])) {
            name += input[inputPos];
            inputPos++;
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

        if (isTokenString(name)) {
            // reset position
            inputPos = initialInputPos;
            return null;
        } else {
            return new VariableToken(name);
        }
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
