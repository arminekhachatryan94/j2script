package j2script;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import j2script.*;
import org.junit.Test;

public class TypeCheckerExpTest {
    // use null if there should be a type error
    public void assertExpType(final Type expected, final Exp exp) {
        try {
            final Type received = Typechecker.expTypeForTesting(exp);
            assertTrue("Expected type error; got: " + received.toString(),
                       expected != null);
            assertEquals(expected, received);
        } catch (final TypeErrorException e) {
            assertTrue("Unexpected type error: " + e.getMessage(),
                       expected == null);
        }
    }
}
