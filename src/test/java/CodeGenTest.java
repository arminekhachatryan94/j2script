package j2script;
import java.util.List;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import org.junit.Assert;
import static org.junit.Assert.assertArrayEquals;
import java.io.File;
import java.util.Scanner;
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
import static org.junit.Assert.assertEquals;

public class CodeGenTest {

	public void assertResult(String expected, Exp expression) throws IOException {
		
		try {
			// Codegen.writeExptoFile(expression, file);
			Codegen code = new Codegen();
			//code.compileExp(expression);
			final File file = File.createTempFile("test", ".js");
			code.writeExptoFile(expression, file);
			//code.writeCompleteFile(file);
			final String output = readFile(file);
			//System.out.println(output);
			//System.out.println(expected);
			assertEquals(expected, output);
			//Assert.assertThat(output.equals(expected));
			//assertTrue(expected.equals(output));
			file.delete();

		} catch (Exception e) {
			System.out.println("somethings up");
		}

	}

	private String readFile(File file) throws IOException {
		// File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int)file.length());        

		try (Scanner scanner = new Scanner(file)) {
			while(scanner.hasNextLine()) {
			fileContents.append(scanner.nextLine());// + System.lineSeparator());
			}
			return fileContents.toString();
		}
	}

	/* Number Expressions */
	@Test
	public void testInt() throws IOException {
	        assertResult("99", new NumberExp(99));
	}

	@Test
	public void testNegativeInt() throws IOException {
	        assertResult("-99", new NumberExp(-99));
	}

	/* Binop Expressions */
	@Test // 1 + 2
	public void testAddition() throws IOException {
	        assertResult("1 + 2", new BinopExp(new NumberExp(1), new PlusOp(), new NumberExp(2)));
	}

	@Test // 2 - 2 
	public void testSubtraction() throws IOException {
	        assertResult("2 - 2", new BinopExp(new NumberExp(2),
			new MinusOp(),
			new NumberExp(2)));
	}

	@Test // 1 - 2 
	public void testSubtractionWithNegativeResult() throws IOException {
	        assertResult("1 - 2", new BinopExp(new NumberExp(1),
			new MinusOp(),
			new NumberExp(2)));
	}

	@Test // 1 - -2 
	public void testAdditionWithSubtractionOP() throws IOException {
	        assertResult("1 - -2", new BinopExp(new NumberExp(1),
			new MinusOp(),
			new NumberExp(-2)));
	}

	@Test // 2 * 2 
	public void testMult() throws IOException {
	        assertResult("2 * 2", new BinopExp(new NumberExp(2),
			new MultOp(),
			new NumberExp(2)));
	}

	@Test // 2 * -2 
	public void testMultWithNegative() throws IOException {
	        assertResult("2 * -2", new BinopExp(new NumberExp(2),
			new MultOp(),
			new NumberExp(-2)));
	}

	@Test // -2 * -2
	public void testMultWithBothNegative() throws IOException {
	        assertResult("-2 * -2", new BinopExp(new NumberExp(-2),
			new MultOp(),
			new NumberExp(-2)));
	}

	@Test // 2 / 2
	public void testDiv() throws IOException {
	        assertResult("2 / 2", new BinopExp(new NumberExp(2),
			new DivOp(),
			new NumberExp(2)));
	}

	/*@Test // 2 / 0 = 0
	public void testDiv() throws IOException {
	        assertResult(null, new BinopExp(new NumberExp(2),
		new DivOp(),
		new NumberExp(2)));
	}*/

	@Test // 2 / 4
	public void testDivTwo() throws IOException {
	        assertResult("2 / 4", new BinopExp(new NumberExp(2),
			new DivOp(),
			new NumberExp(4)));
	}


	/* Complex Algebraic Equations */
	@Test // 1 - 2 / 3
	public void testArithmeticEquation() throws IOException {
	        assertResult("1 - 2 / 3", new BinopExp(new NumberExp(1),
			new MinusOp(),
			new BinopExp(new NumberExp(2), new DivOp(), new NumberExp(3))
		));
	}

	@Test // 1 + 2 - 3
	public void testArithmeticEquationTwo() throws IOException {
	        assertResult("1 + 2 - 3", new BinopExp(new BinopExp(new NumberExp(1),
			new PlusOp(),
			new NumberExp(2)),
			new MinusOp(),
			new NumberExp(3)));
	}

	@Test // 1 * 2 - 3
	public void testArithmeticEquationThree() throws IOException {
	        assertResult("1 * 2 - 3", new BinopExp(new BinopExp(new NumberExp(1),
			new MultOp(),
			new NumberExp(2)),
			new MinusOp(),
			new NumberExp(3)));
	}

	@Test // 3 * 2 / 3 = 1
	public void testArithmeticEquationFour() throws IOException {
	        assertResult("3 * 2 / 3", new BinopExp(new BinopExp(new NumberExp(3),
			new MultOp(),
			new NumberExp(2)),
			new DivOp(),
			new NumberExp(3)));
	}

	/* StringExp */
	@Test 
	public void testString() throws IOException {
	        assertResult("\"Food\"", new StringExp("Food"));
	}

	/* Method Expression */
	@Test // methodOne(4, stringName)
	public void testFucntionCall() throws IOException {
		Exp expressions [] = {new NumberExp(4), new VariableExp("stringName")};
	        assertResult("methodOne(4, stringName)", new VarMethodExp(new Variable("var"), new MethodName("methodOne"), expressions));
	}

	/* Class Expression */
	@Test // new Foo(4, "hello")
	public void testClassObjects() throws IOException {
		Exp expressions [] = {new NumberExp(4), new StringExp("hello")};
	        assertResult("Foo(4, \"hello\")", new ClassExp(new ClassName("Foo"), expressions));
	}

	/* Class Expression */
	@Test // new Foo()
	public void testClassObjectsWithNoParameters() throws IOException {
		Exp expressions [] = {};
	        assertResult("Foo()", new ClassExp(new ClassName("Foo"), expressions));
	}
	
}