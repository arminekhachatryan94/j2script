package j2script;
import java.util.List;
import java.util.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import java.util.Arrays;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


import j2script.*;
import j2script.expressions.*;
import j2script.operators.*;
import j2script.tokens.*;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class ParserTest {
  public void assertParses(final Token[] tokens, final Exp expected) {
        final Parser parser = new Parser(tokens);
        try {
            final Exp received = parser.parseExp();
            assertTrue("Expected parse failure; got: " + received, expected != null);
            assertEquals(expected, received);
        } catch (final ParserException e) {
            assertTrue(("Unexpected parse failure for " + Arrays.toString(tokens) + 
            			": " + e.getMessage()), expected == null);
        }
    }

    @Test
    public void testIntegerParsing() {
        assertParses(new Token[]{ new NumberToken(123) }, new NumberExp(123));
    }

    @Test
    public void testVariableParsing() {
        assertParses(new Token[]{ new VariableToken("foo") }, new VariableExp("foo"));
    }

	@Test
	public void testParsesIf() {
	    final Token[] tokens = { new IfToken(),
	                                 new LeftParenToken(),
	                                 new NumberToken(4),
	                                 new RightParenToken(),
	                                 new LeftCurlyToken(),
	                                 new NumberToken(2),
	                                 new RightCurlyToken(),
	                                 new ElseToken(),
	                                 new LeftCurlyToken(),
	                                 new NumberToken(0),
	                                 new RightCurlyToken() };
	    final Exp expected = new IfExp(new NumberExp(4),
	                                       new NumberExp(2),
	                                       new NumberExp(0));
	    assertParses(tokens, expected);
	}

    @Test
    public void testBinaryParsing() {
    	final Token[] tokens = { new NumberToken(4),
    							 new AddToken(),
    							 new NumberToken(4)
    							 };

    	final Exp expected = new BinaryExp(new NumberExp(4), new PlusOp(), new NumberExp(4));
    	assertParses(tokens, expected);
    }


    @Test /*passes*/
        public void testParserExceptionWithEmptyTokens() {
    	final Token[] tokens = {};
    	assertParses(tokens, null);    	
    }


}