package j2script;
import java.util.List;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import java.io.File;
import java.io.IOException;
import j2script.*;
import j2script.tokens.*;
import j2script.access.*;
import j2script.declarations.*;
import j2script.expressions.*;
import j2script.names.*;
import j2script.operators.*;
import j2script.statements.*;
import j2script.types.*;
import org.junit.Test;

public class CodeGenTestsExp {

	public void assertResult(String expected, Exp expression) throws IOException{}

	/*Number Expressions*/
	@Test
	public void testInt() throws IOException {
	        assertResult("99", new NumberExp(99));
	}

	@Test
	public void testNegativeInt() throws IOException {
	        assertResult("-99", new NumberExp(-99));
	}


	/*Binop Expressions*/
	@Test //1+2 = 3
	public void testAddition() throws IOException {
	        assertResult("3", new BinopExp(new NumberExp(1),
	new PlusOp(),
	new NumberExp(2)));
	}

	@Test //2-2 = 0
	public void testSubtraction() throws IOException {
	        assertResult("0", new BinopExp(new NumberExp(2),
	new MinusOp(),
	new NumberExp(2)));
	}

	@Test //1 -2 = -1
	public void testSubtractionWithNegativeResult() throws IOException {
	        assertResult("-1", new BinopExp(new NumberExp(1),
	new MinusOp(),
	new NumberExp(2)));
	}

	@Test //1- -2 = 3
	public void testAdditionWithSubtractionOP() throws IOException {
	        assertResult("3", new BinopExp(new NumberExp(1),
	new MinusOp(),
	new NumberExp(-2)));
	}

	@Test //2*2 = 4
	public void testMult() throws IOException {
	        assertResult("4", new BinopExp(new NumberExp(2),
	new MultOp(),
	new NumberExp(2)));
	}

	@Test //2*-2 = -4
	public void testMultWithNegative() throws IOException {
	        assertResult("-4", new BinopExp(new NumberExp(2),
	new MultOp(),
	new NumberExp(-2)));
	}

	@Test //-2*-2 = 4
	public void testMultWithBothNegative() throws IOException {
	        assertResult("4", new BinopExp(new NumberExp(-2),
	new MultOp(),
	new NumberExp(-2)));
	}

	@Test //2/2 = 0
	public void testDiv() throws IOException {
	        assertResult("0", new BinopExp(new NumberExp(2),
	new DivOp(),
	new NumberExp(2)));
	}

	/*@Test //2/0 = 0
	public void testDiv() throws IOException {
	        assertResult(null, new BinopExp(new NumberExp(2),
	new DivOp(),
	new NumberExp(2)));
	}*/

	@Test //2/4 = 0
	public void testDivTwo() throws IOException {
	        assertResult("0", new BinopExp(new NumberExp(2),
	new DivOp(),
	new NumberExp(2)));
	}


	/**Complex Algebraic Equations**/

	@Test // 1-2/3 =0
	public void testArithmeticEquation() throws IOException {
	        assertResult("0", new BinopExp(new NumberExp(1),
	new MinusOp(),
	new BinopExp(new NumberExp(2), new DivOp(), new NumberExp(3))
	));
	}

	@Test // 1+2-3 =0
	public void testArithmeticEquationTwo() throws IOException {
	        assertResult("0", new BinopExp(new BinopExp(new NumberExp(1),
				new PlusOp(),
				new NumberExp(2)),
				new MinusOp(),
				new NumberExp(3)));
	}

	@Test // 1*2-3 =-1
	public void testArithmeticEquationThree() throws IOException {
	        assertResult("-1", new BinopExp(new BinopExp(new NumberExp(1),
				new MultOp(),
				new NumberExp(2)),
				new MinusOp(),
				new NumberExp(3)));
	}

	@Test // 3*2/3 =1
	public void testArithmeticEquationFour() throws IOException {
	        assertResult("1", new BinopExp(new BinopExp(new NumberExp(3),
				new MultOp(),
				new NumberExp(2)),
				new DivOp(),
				new NumberExp(3)));
	}

	/**StringExp**/
	@Test 
	public void testString() throws IOException {
	        assertResult("Food", new StringExp("Food"));
	}
/*
	/*Method Expression
	@Test //var.methodOne(4, "hello world")
	public void testFucntionCall() throws IOException {
		Exp expressions [] = {new NumberExp(4), new StringExp("hello world")};
	        assertResult("var", new VarMethodExp(new Variable("var"), new MethodName("methodOne"), expressions));
	}
*/
	/*Class Expression*/
	@Test //new Foo(4, "hello")
	public void testClassObjects() throws IOException {
		Exp expressions [] = {new NumberExp(4), new StringExp("hello")};
	        assertResult("Foo(4, hello)", new ClassExp(new ClassName("Foo"), expressions));
	}

	/*Class Expression*/
	@Test //new Foo(4, "hello")
	public void testClassObjectsWithNoParameters() throws IOException {
		Exp expressions [] = {};
	        assertResult("Foo()", new ClassExp(new ClassName("Foo"), expressions));
		
		
	}




}