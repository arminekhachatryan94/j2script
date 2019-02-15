package j2script;
import java.util.List;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import j2script.*;
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
    public void testTokenizeInteger() {
        assertTokenizes("123", new Token[]{
            new NumberToken(123)
        });
    }
}
