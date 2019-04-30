package j2script;
import java.util.List;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import org.junit.Assert;
import java.util.Arrays;
import static org.junit.Assert.assertArrayEquals;
import java.io.File;
import java.util.ArrayList;
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
			Codegen code = new Codegen();
			final File file = File.createTempFile("test", ".js");
			code.writeExptoFile(expression, file);
			final String output = readFile(file);
			assertEquals(expected, output);
			file.delete();

		} catch (Exception e) {
			System.out.println("somethings up");
		}

	}


	public void assertResultStatements(String expected, Statement stmt) throws IOException {
		
		try {
			Codegen code = new Codegen();
			final File file = File.createTempFile("test", ".js");
			code.writeStatementstoFile(stmt, file);
			final String output = readFile(file);
			assertEquals(expected, output);
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


	@Test // 2 * 2 
	public void testMult() throws IOException {
	        assertResult("2 * 2", new BinopExp(new NumberExp(2),
			new MultOp(),
			new NumberExp(2)));
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

	@Test // 3 * 2 / 3 
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
		List<Exp> expressions = new ArrayList<>();
		 expressions.add(new NumberExp(4));
		 expressions.add(new StringExp("hello"));
	        assertResult("Foo(4, \"hello\")", new ClassExp(new ClassName("Foo"), expressions));
	}

	/* Class Expression */
	@Test // new Foo()
	public void testClassObjectsWithNoParameters() throws IOException {
		List<Exp> expressions = new ArrayList<>();
	        assertResult("Foo()", new ClassExp(new ClassName("Foo"), expressions));
	}

	@Test
	public void testIfTrue() throws IOException {
		/*
		if (true) x = 0;
		else x = 1;
		*/
		assertResultStatements("if (true) { x = 0 } else { x = 1}", new IfStatement(new BoolExp(true), new VarAssignment(new Variable("x"), new NumberExp(0)), new VarAssignment(new Variable("x"), new NumberExp(1))));
	}

	@Test
	public void testIfFalse() throws IOException {
		/*
		if (false) x = 0;
		else x = 1;
		*/
		assertResultStatements("if (false) { x = 0 } else { x = 1}", new IfStatement(new BoolExp(false), new VarAssignment(new Variable("x"), new NumberExp(0)), new VarAssignment(new Variable("x"), new NumberExp(1))));
	}

	@Test
	public void testWhileLoop() throws IOException{
		/*
		while(true)
			x = 0;
		*/
		assertResultStatements("while(true) {x = 0}", new WhileStatement(new BoolExp(true), new VarAssignment(new Variable("x"), new NumberExp(0))));

	}

	@Test
	public void testWhileLoopWithIfStmt() throws IOException {
		/*
		while(true)
			if(true)
			x =0;
			else
			x =1;
		*/
		assertResultStatements("while(true) {if (true) { x = 0 } else { x = 1}}", new WhileStatement(new BoolExp(true), 
			new IfStatement(new BoolExp(true), 
				new VarAssignment(new Variable("x"), 
					new NumberExp(0)), 
				new VarAssignment(new Variable("x"), new NumberExp(1)))));

	}

	@Test
	public void testWhileLoopWithIfStmtWithBreaks() throws IOException{
		/*
		while(true)
			if(true)
			break;
			else
			break;
		*/
		assertResultStatements("while(true) {if (true) { break } else { break}}", new WhileStatement(new BoolExp(true), 
			new IfStatement(new BoolExp(true), 
				new BreakStatement(), 
				new BreakStatement())));

	}

	@Test
	public void testNestedIfStmts() throws IOException{
		/*
		if(true)
			if(true)
				x = 1;
			else 
				x = 2;
		else
			x = 3;
		*/
		Statement ifNestedStm = new IfStatement(
			new BoolExp(true), 
			new VarAssignment(
				new Variable("x"), new NumberExp(1)
				), 
			new VarAssignment(new Variable("x"), new NumberExp(2)));

		assertResultStatements("if (true) { if (true) { x = 1 } else { x = 2} } else { x = 3}", new IfStatement(new BoolExp(true), ifNestedStm, new VarAssignment(new Variable("x"), new NumberExp(3))));

	}

	@Test
	public void testNestedWhileStmts() throws IOException{
		/*
		while(x) 
			while(y)
				x = true;
		*/

		Statement whileNested = new WhileStatement(new VariableExp("y"), new VarAssignment(new Variable("x"), new BoolExp(true)));
		assertResultStatements("while(x) {while(y) {x = true}}", new WhileStatement(new VariableExp("x"), whileNested));

	}

	@Test
	public void testIfTrueWithInequality() throws IOException {
		/*
		if (x<2) x = 0;
		else x = 1;
		*/

		assertResultStatements("if (x < 2) { x = 0 } else { x = 1}", new IfStatement(new BinopExp(new VariableExp("x"), new LessThanOp(), new NumberExp(2)), new VarAssignment(new Variable("x"), new NumberExp(0)), new VarAssignment(new Variable("x"), new NumberExp(1))));




	}

	@Test
	public void testIfTrueWithInequalityWithNested() throws IOException {
		/*
		if(x<22)
			if(x==2)
				x = 1;
			else 
				x = 2;
		else
			x = 3;
		*/

		Statement ifNestedStm = new IfStatement(new BinopExp(new VariableExp("x"), new EqualsOp(), new NumberExp(2)), new VarAssignment(new Variable("x"), new NumberExp(1)), new VarAssignment(new Variable("x"), new NumberExp(2)));

		assertResultStatements("if (x < 22) { if (x == 2) { x = 1 } else { x = 2} } else { x = 3}", new IfStatement(new BinopExp(new VariableExp("x"), new LessThanOp(), new NumberExp(22)), ifNestedStm, new VarAssignment(new Variable("x"), new NumberExp(3))));
	}

	@Test
	public void testWhileLoopWithIfStmtWithBreaksAndEquality() throws IOException {
		/*
		while(x<22)
			if(x==21)
			break;
			else
			x=21;
		*/	
			assertResultStatements("while(x < 22) {if (x == 21) { break } else { break}}", 
				new WhileStatement(
					new BinopExp(new VariableExp("x"), new LessThanOp(), new NumberExp(22)), 
					new IfStatement(
						new BinopExp(new VariableExp("x"), new EqualsOp(), new NumberExp(21)), 
						new BreakStatement(), 
						new BreakStatement()
						)
					)
				);

	}


/***
	@Test
	public void testIfWithVariableExp(){

		String expected = "{
			int x = 22;
			boolean y = true;
			if(y) x = 0;
			else x = 1;
		}";

		Statement xStm = new VarDecAssignement(new VarDec(new IntType(), new Variable("x")), new NumberExp(22));
		Statement yStm = new VarDecAssignement(new VarDec(new BooleanType(), new Variable("x")), new BoolExp(true));
		Statement ifStm = new IfStatement(new VariableExp("y"), new VarAssignment(new Variable("x"), new NumberExp(0)), new VarAssignment(new Variable("x"), new NumberExp(1))));

		List<Statement> statements = new ArrayList<>();
		statements.add(xStm);
		statements.add(yStm);
		statements.add(ifStm);

		assertResultStatements(expected, new Block(statements));
	}

	@Test
	public void testWhileLoop() {

		String expected = "
		{
		boolean y = true;
		while(y)
			y = false;
		}
		";

		Statement yStm = new VarDecAssignement(new VarDec(new BooleanType(), new Variable("x")), new BoolExp(true));
		Statement whileStm = new WhileStatement(new VariableExp("y"), new VarAssignment(new Variable("y"), new BoolExp(false)));

		List<Statement> statements = new ArrayList<>();
		statements.add(yStm);
		statements.add(whileStm);

		assertResultStatements(expected, new Block(statements));

	}

	@Test
	public void nestedIfStatements(){

		String expected = "
		{
		int x = 0;
		if(true)
			if(true)
				x = 1;
			else 
				x = 2;
		else
			x = 3;
		}
		";

		Statement xStm = new VarDecAssignement(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
		Statement ifNestedStm = new IfStatement(new BoolExp(true), new VarAssignment(new Variable("x"), new NumberExp(1)), new VarAssignment(new Variable("x"), new NumberExp(2))));
		Statement ifStm = new IfStatement(new BoolExp(true), ifNestedStm, new VarAssignment(new Variable("x"), new NumberExp(3))));

		List<Statement> statements = new ArrayList<>();
		statements.add(xStm);
		statements.add(ifStm);

		assertResultStatements(expected, new Block(statements));

	}

	@Test
	public void nestedIfElseStatements(){

		String expected = "
		{
		int x = 0;
		if(true)
			if(true)
				x = 1;
			else 
				x = 2;
		else
			if(true) 
				x = 3;
			else 
				x = 4;
		}
		";

		Statement xStm = new VarDecAssignement(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
		Statement ifNestedStm = new IfStatement(new BoolExp(true), new VarAssignment(new Variable("x"), new NumberExp(1)), new VarAssignment(new Variable("x"), new NumberExp(2))));
		Statement elseNestedStm = new IfStatement(new BoolExp(true), new VarAssignment(new Variable("x"), new NumberExp(3)), new VarAssignment(new Variable("x"), new NumberExp(4))));
		Statement ifStm = new IfStatement(new BoolExp(true), ifNestedStm, elseNestedStm));

		List<Statement> statements = new ArrayList<>();
		statements.add(xStm);
		statements.add(ifStm);

		assertResultStatements(expected, new Block(statements));

	}

	@Test
	public void nestedWhileIfStatements(){

		String expected = "
		{
		int x = 0;
		while(true) 
			if(true)
				if(true)
					x = 1;
					break;
				else 
					x = 2;
			else
				x = 3;
		}
		";

		Statement xStm = new VarDecAssignement(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
		List<Statement> nested = new ArrayList<>();
		nested.add(new VarAssignment(new Variable("x"), new NumberExp(1)));
		nested.add(new BreakStatement());
		Statement ifNestedStm = new IfStatement(new BoolExp(true), new Block(nested), new VarAssignment(new Variable("x"), new NumberExp(2))));
		Statement ifStm = new IfStatement(new BoolExp(true), ifNestedStm, new VarAssignment(new Variable("x"), new NumberExp(3))));
		Statement whileStm = new WhileStatement(new BoolExp(true), ifStm);
		

		List<Statement> statements = new ArrayList<>();
		statements.add(xStm);
		statements.add(ifStm);

		assertResultStatements(expected, new Block(statements));

	}***/
	
}