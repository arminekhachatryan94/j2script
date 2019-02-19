package j2script;
import java.util.List;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import j2script.*;
import j2script.tokens.*;
import org.junit.Test;

public class TokenizerTest {
    public void assertTokenizes(final String input,
                                final Token[] expected) {
        final Tokenizer tokenizer = new Tokenizer(input.toCharArray());
        try {
            final List<Token> received = tokenizer.tokenize();
            assertTrue("Expected tokenization failure, got: " + received,
                    expected != null);
            assertArrayEquals(expected,
                    received.toArray(new Token[received.size()]));
        } catch (final TokenizerException e) {
            assertTrue(("Unexpected tokenization failure for \"" +
                            input + "\": " + e.getMessage()),
                    expected == null);
        }
    }

    @Test
    public void testTokenizeSingleDigitInteger() {
        assertTokenizes("0", new Token[]{ new NumberToken(0) });
    }

    @Test
    public void testTokenizeInteger() {
        assertTokenizes("123", new Token[]{ new NumberToken(123) });
    }

    @Test
    public void testTokenizeIntegerLeadingWhitespace() {
        assertTokenizes("  123", new Token[]{ new NumberToken(123) });
    }

    @Test
    public void testTokenizeIntegerTrailingWhitespace() {
        assertTokenizes("123   ", new Token[]{ new NumberToken(123) });
    }

    @Test
    public void testTokenizeIntegerLeadingAndTrailingWhitespace() {
        assertTokenizes("  123  ", new Token[]{ new NumberToken(123) });
    }

    @Test
    public void testTokenizeVariableSingleLetter() {
        assertTokenizes("x", new Token[]{ new VariableToken("x") });
    }

    @Test
    public void testTokenizeVariableMultiLetter() {
        assertTokenizes("foo", new Token[]{ new VariableToken("foo") });
    }

    @Test
    public void testTokenizeVariableStartsWithIf() {
        assertTokenizes("ifx", new Token[]{ new VariableToken("ifx") });
    }

    @Test
    public void testTokenizeIf() {
        assertTokenizes("if", new Token[]{ new IfToken() });
    }

    @Test
    public void testTokenizeSingleChars() {
        assertTokenizes("+-*/(){}", new Token[]{
            new AddToken(),
            new SubtractToken(),
            new MultiplyToken(),
            new DivToken(),
            new LeftParenToken(),
            new RightParenToken(),
            new LeftCurlyToken(),
            new RightCurlyToken()
        });
    }

    @Test
    public void testIntVariableEqualNumberToken() {
        assertTokenizes("int xyz=25", new Token[]{
            new IntToken(),
            new VariableToken("xyz"),
            new EqualToken(),
            new NumberToken(25)
        });
    }

    @Test
    public void testTokenizeIntermixed() {
        assertTokenizes("*if+foo-", new Token[]{
            new MultiplyToken(),
            new IfToken(),
            new AddToken(),
            new VariableToken("foo"),
            new SubtractToken()
        });
    }

    @Test
    public void testTokenizeElse() {
        assertTokenizes("else", new Token[]{ new ElseToken() });
    }

    @Test
    public void testTokenizeVariableStartsWithElse() {
        assertTokenizes("elsex", new Token[]{ new VariableToken("elsex") });
    }

    @Test
    public void testTokenizeVariableEndsWithElse() {
        assertTokenizes("xelse", new Token[]{ new VariableToken("xelse") });
    }

    @Test
    public void testTokenizeIfExpression() {
        assertTokenizes("if (1) { x } else { y }", new Token[]{
            new IfToken(),
            new LeftParenToken(),
            new NumberToken(1),
            new RightParenToken(),
            new LeftCurlyToken(),
            new VariableToken("x"),
            new RightCurlyToken(),
            new ElseToken(),
            new LeftCurlyToken(),
            new VariableToken("y"),
            new RightCurlyToken()
        });
    }

    @Test
    public void testTokenizePublic() {
        assertTokenizes("public", new Token[]{
            new PublicToken()
        });
    }

    @Test
    public void testTokenizePrivate() {
        assertTokenizes("private", new Token[]{
            new PrivateToken()
        });
    }

    @Test
    public void testTokenizeExtends() {
        assertTokenizes("extends", new Token[]{
            new ExtendsToken()
        });
    }
}
