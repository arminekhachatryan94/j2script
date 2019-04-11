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


    @Test
    public void testIntegerType() { //2
        final Type type = new IntType();
        final Exp expression = new NumberExp(2);
        assertExpType(type, expression);
    }


    @Test
    public void testStringType() { //"2"
        final Type type = new StringType();
        final Exp expression = new StringExp("2");
        assertExpType(type, expression);
    }


    @Test
    public void testBoolTypeTrue() { //true
        final Type type = new BooleanType();
        final Exp expression = new BoolExp(true);
        assertExpType(type, expression);
    }

    @Test
    public void testBoolTypeTrue() { //false
        final Type type = new BooleanType();
        final Exp expression = new BoolExp(false);
        assertExpType(type, expression);
    }


    @Test
    public void testADDBinopNumbersExpressionsType() { //2+2
        final Type type = new IntType();
        final Exp expression = new BinopExp(new NumberExp(2), new PlusOp(), new NumberExp(2));
        assertExpType(type, expression);
    }

    @Test
    public void testADDBinopNumbersAndStringType() { // 2+ "whatever"
        //final Type type = new StringType();
        final Exp expression = new BinopExp(new NumberExp(2), new PlusOp(), new VariableExp("whatever"));
        assertExpType(null, expression);
    }

    @Test
    public void testMINUSBinopNumbersExpressionsType() { //2-2
        final Type type = new IntType();
        final Exp expression = new BinopExp(new NumberExp(2), new MinusOp(), new NumberExp(2));
        assertExpType(type, expression);
    }

    @Test
    public void testMINUSBinopNumberAndStringExpressionsType() { //2-"whatever"
        //final Type type = new IntType();
        final Exp expression = new BinopExp(new NumberExp(2), new MinusOp(), new VariableExp("whatever"));
        assertExpType(null, expression);
    }

    @Test
    public void testDIVBinopNumbersExpressionsType() { //2/2
        final Type type = new IntType();
        final Exp expression = new BinopExp(new NumberExp(2), new DivOp(), new NumberExp(2));
        assertExpType(type, expression);
    }

    @Test
    public void testDIVBinopNumberAndStringsExpressionsType() { //2/whatever
        //final Type type = new IntType();
        final Exp expression = new BinopExp(new NumberExp(2), new DivOp(), new VariableExp("whatever"));
        assertExpType(null, expression);
    }

    @Test
    public void testDIVBinopNumberWithZeroExpressionsType() { //2/0
        //final Type type = new IntType();
        final Exp expression = new BinopExp(new NumberExp(2), new DivOp(), new NumberExp(0));
        assertExpType(null, expression);
    }

    @Test
    public void testMultBinopNumbersExpressionsType() { //2*2
        final Type type = new IntType();
        final Exp expression = new BinopExp(new NumberExp(2), new MultOp(), new NumberExp(2));
        assertExpType(type, expression);
    }
        
    @Test
    public void testMULTBinopNumberAndStringsExpressionsType() { //2*whatever
        //final Type type = new IntType();
        final Exp expression = new BinopExp(new NumberExp(2), new MultOp(), new VariableExp("whatever"));
        assertExpType(null, expression);
    }

    


}


/*package j2script;

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
}*/

