package j2script;

import j2script.tokens.*;

import java.util.HashMap;
import java.util.Map;

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
                put("variable", new VariableToken());
                put("Void", new VoidToken());
                put("while", new WhileToken());
            }};

    public Tokenizer(final char[] input) {
        this.input = input;
        inputPos = 0;
    }

}
