/****package j2script;

import java.util.Arrays;
import java.util.List;



import j2script.tokens.*;
import j2script.access.*;
import j2script.declarations.*;
import j2script.expressions.*;
import j2script.names.*;
import j2script.operators.*;
import j2script.statements.*;
import j2script.types.*;
import j2script.ParserException;
import java.util.*;
import java.util.List;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import j2script.*;
import j2script.tokens.*;
import org.junit.Test;

public class ParserTest {
	public void assertParses(final Token[] tokens, final Program expected) {
		final Parser parser = new Parser(tokens);
		try {
			final Program received = parser.parse();
			assertTrue("Expected parse failure; got: " + received,
					expected != null);
			assertEquals(expected, received);
		} catch (final ParserException e) {
			assertTrue(("Unexpected parse failure for " +
							Arrays.toString(tokens) +
							": " + e.getMessage()),
					expected == null);
		}
	}




	@Test
	public void testComplexAlgebraicExpressionStatement(){ //int x = 5+ ((1+2) x 4);
		final Token [] tokens = { new IntToken(),
				new VariableToken("x"),
				new EqualToken(),
				new NumberToken(5),
				new AddToken(),
				new LeftParenToken(),
				new LeftParenToken(),
				new NumberToken(1),
				new AddToken(),
				new NumberToken(2),
				new RightParenToken(),
				new MultiplyToken(),
				new NumberToken(4),
				new RightParenToken(),
				new SemiToken(),
		};

		final Statement st = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")),
				new BinopExp(new NumberExp(5),
						new PlusOp(),
						new BinopExp((new BinopExp(new NumberExp(1),
								new PlusOp(),
								new NumberExp(2))),
								new MultOp(),
								new NumberExp(4)))

		);

		Program program = new Program(null, st);
		assertParses(tokens, program);
	}

	@Test
	public void testArithmeticPrecedenceStatement() { // x = 1-2/3;
		final Token[] tokens = {
				new VariableToken("x"),
				new EqualToken(),
				new NumberToken(1),
				new MinusToken(),
				new NumberToken(2),
				new DivToken(),
				new NumberToken(3),
				new SemiToken() };

		final Statement st = new VarAssignment(new Variable("x"),
				new BinopExp(new NumberExp(1),
						new MinusOp(),
						new BinopExp(new NumberExp(2),
								new DivOp(),
								new NumberExp(3))));
		Program program = new Program(null, st);
		assertParses(tokens, program);
	}

	@Test
	public void testArithmeticPrecedenceWithParensStatement() {
		final Token[] tokens = { new VariableToken("x"),
				new EqualToken(),
				new LeftParenToken(),
				new NumberToken(1),
				new MinusToken(),
				new NumberToken(2),
				new RightParenToken(),
				new DivToken(),
				new NumberToken(3),
				new SemiToken() };

		final Statement st = new VarAssignment(new Variable("x"), new BinopExp(new BinopExp(new NumberExp(1),
				new MinusOp(),
				new NumberExp(2)),
				new DivOp(),
				new NumberExp(3)));


		Program program = new Program(null, st);
		assertParses(tokens, program);
	}

	@Test
	public void testArithmeticLeftAssociativeStatement() {
		final Token[] tokens = { new VariableToken("x"),
				new EqualToken(),
				new NumberToken(1),
				new AddToken(),
				new NumberToken(2),
				new MinusToken(),
				new NumberToken(3),
				new SemiToken() };

		final Statement st = new VarAssignment(new Variable("x"), new BinopExp(new BinopExp(new NumberExp(1),
				new PlusOp(),
				new NumberExp(2)),
				new MinusOp(),
				new NumberExp(3)));
		Program program = new Program(null, st);
		assertParses(tokens, program);
	}

	@Test
	public void testStringParsing() { //comp = "Comp430";
		final Token[] tokens ={new VariableToken("comp"),
				new EqualToken(),
				new VariableToken("Comp430"),
		};
		final Statement st = new VarAssignment( new Variable("comp"),
				new VariableExp("Comp430"));




		Program program = new Program(null, st);
		assertParses(tokens, program);
	}

	@Test
	public void testVarDecParsing() { //String poop = "poo";
		final Token[] tokens ={new StringToken(),
				new VariableToken("poop"),
				new EqualToken(),
				new VariableToken("poo")
		};
		final Statement st = new VarDecAssignment( new VarDec(new StringType(),
				new Variable("poop")), new VariableExp("poo"));




		Program program = new Program(null, st);
		assertParses(tokens, program);
	}

	@Test
	public void testIfStatementParsing() { //if(1) foo =2; else foo=3;

		final Token [] tokens = {new IfToken(),
				new LeftParenToken(),
				new NumberToken(1),
				new RightParenToken(),
				new VariableToken("foo"),
				new EqualToken(),
				new NumberToken(2),
				new SemiToken(),
				new ElseToken(),
				new VariableToken("foo"),
				new EqualToken(),
				new NumberToken(3),
				new SemiToken()
		};

		final Statement st = new IfStatement(new NumberExp(1),
				new VarAssignment(new Variable("foo"), new NumberExp(2)),
				new VarAssignment(new Variable("foo"), new NumberExp(3)));

		Program program = new Program(null, st);
		assertParses(tokens, program);

	}

	@Test
	public void testIfStatementWithABlockParsing() { //if(1) {foo =2; println(found it)} else foo=3;

		final Token [] tokens = {
				new IfToken(),
				new LeftParenToken(),
				new NumberToken(1),
				new RightParenToken(),
				new LeftCurlyToken(),
				new VariableToken("foo"),
				new EqualToken(),
				new NumberToken(2),
				new SemiToken(),
				new PrintToken(),
				new LeftParenToken(),
				new VariableToken("found it"),
				new RightParenToken(),
				new RightCurlyToken(),
				new ElseToken(),
				new VariableToken("foo"),
				new EqualToken(),
				new NumberToken(3),
				new SemiToken() };




		final List<Statement> statements = new ArrayList<>();
		statements.add(new VarAssignment(new Variable("foo"), new NumberExp(2)));
		statements.add(new PrintStatement("found it"));
		final Statement st = new IfStatement(new NumberExp(1),
				new Block(statements),
				new VarAssignment(new Variable("foo"), new NumberExp(3)));


		Program program = new Program(null, st);
		assertParses(tokens, program);

	}

	@Test
	public void testWhileStatementParsing() { //while(2)  println("still in while loop");
		final Token[] tokens = { new WhileToken(),
				new LeftParenToken(),
				new NumberToken(2),
				new RightParenToken(),
				new PrintToken(),
				new LeftParenToken(),
				new VariableToken("still in while loop"),
				new RightParenToken(),
				new SemiToken()

		};

		final Statement stm = new WhileStatement(new NumberExp(2), new PrintStatement("still in while loop"));
		Program program = new Program(null, stm);
		assertParses(tokens, program);


	}

	@Test
	public void testWhileStatmentWithIfElseStatementParsing() { //while(2) if(2) break; else println("still in while loop");
		final Token[] tokens = { new WhileToken(),
				new LeftParenToken(),
				new NumberToken(2),
				new RightParenToken(),
				new IfToken(),
				new LeftParenToken(),
				new NumberToken(2),
				new RightParenToken(),
				new BreakToken(),
				new ElseToken(),
				new PrintToken(),
				new LeftParenToken(),
				new VariableToken("still in while loop"),
				new RightParenToken(),
				new SemiToken(),

		};

		final Statement stm = new WhileStatement(new NumberExp(2),   new IfStatement(new NumberExp(2),
				new BreakStatement(),
				new PrintStatement("still in while loop")));
		Program program = new Program(null, stm);
		assertParses(tokens, program);


	}

	@Test
	public void testBlockStatmentParsing() { //{ int x = 0; println("halo");}
		final Token[] tokens = { new LeftCurlyToken(),
				new IntToken(),
				new VariableToken("x"),
				new EqualToken(),
				new NumberToken(0),
				new SemiToken(),
				new PrintToken(),
				new LeftParenToken(),
				new VariableToken("halo"),
				new RightParenToken(),
				new SemiToken(),

		};

		List <Statement> statements = new ArrayList<>();
		statements.add(new VarDecAssignment(new VarDec(new IntType(),
				new Variable("x")), new NumberExp(0)));

		final Statement stm = new Block(statements);
		Program program = new Program(null, stm);
		assertParses(tokens, program);


	}


	@Test
	public void testAClassDefWithConstructorAndInstanceVariable() {
    	/*
    	Class ClassFoo{
			private int x;
			CLassFoo(int parameterOne)
				x = parameterOne;
    	}
    	

		final Token[] tokens = {new ClassToken(),
				new VariableToken("ClassFoo"),
				new LeftCurlyToken(),
				new PrivateToken(),
				new IntToken(),
				new VariableToken("x"),
				new SemiToken(),
				new VariableToken("ClassFoo"),
				new LeftParenToken(),
				new IntToken(),
				new VariableToken("parameterOne"),
				new RightParenToken(),
				new VariableToken("x"),
				new EqualToken(),
				new VariableToken("parameterOne"),
				new SemiToken(),
				new RightCurlyToken()
		};
		final List<InstanceDec> insDec = new ArrayList<>();
		insDec.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("x"))));
		final VarDec [] varDec = {new VarDec(new IntType(), new Variable("parameterOne"))};
		final List<MethodDef> methodDef = new ArrayList<>();
		final Statement st = new VarAssignment(new Variable("x"), new VariableExp("parameterOne"));
		final ClassDef cd = new ClassDef(new ClassName("ClassFoo"), new Constructor(varDec, new VarAssignment(new Variable("x"), new VariableExp("parameterOne"))), insDec, st,methodDef);

		final List<ClassDef> classdef = new ArrayList<>();
		classdef.add(cd);

		Program program = new Program(classdef, null);
		assertParses(tokens, program);

	}

	@Test
	public void testAClassDefWithConstructorAndInstanceVariableAndAVoidMethod(){

    	/*
    	Class ClassFoo{
			private int x;
			CLassFoo(int parameterOne)
				x = parameterOne;
			private void methodOne(int four)
				four = 4 + x;
    	}
    	
		final Token[] tokens = {new ClassToken(),
				new VariableToken("ClassFoo"),
				new LeftCurlyToken(),
				new PrivateToken(),
				new IntToken(),
				new VariableToken("x"),
				new SemiToken(),
				new VariableToken("ClassFoo"),
				new LeftParenToken(),
				new IntToken(),
				new VariableToken("parameterOne"),
				new RightParenToken(),
				new VariableToken("x"),
				new EqualToken(),
				new VariableToken("parameterOne"),
				new SemiToken(),
				new PrivateToken(),
				new VoidToken(),
				new VariableToken("methodOne"),
				new LeftParenToken(),
				new IntToken(),
				new VariableToken("four"),
				new RightParenToken(),
				new VariableToken("four"),
				new EqualToken(),
				new NumberToken(4),
				new AddToken(),
				new VariableToken("x"),
				new SemiToken(),
				new RightCurlyToken()
		};
		final List<MethodDef> methodDef = new ArrayList<>();
		final VarDec [] varDec = {new VarDec(new IntType(), new Variable("parameterOne"))};
		methodDef.add(new MethodDef(new PrivateAccess(),
				new VoidType(),
				new MethodName("methodOne"),
				varDec,
				new VarAssignment(new Variable("four"), new BinopExp(new NumberExp(4), new PlusOp(), new VariableExp("x")))));
		final List<InstanceDec> insDec = new ArrayList<>();
		insDec.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("x"))));
		final Statement st = new VarAssignment(new Variable("x"), new VariableExp("parameterOne"));
		//final VarDec [] varDec = {new Vardec(new IntType(), new Variable("parameterOne"))};
		final ClassDef cD = new ClassDef(new ClassName("ClassFoo"),

				new Constructor(varDec,
						new VarAssignment(new Variable("x"),
								new VariableExp("parameterOne"))),
				insDec,
				st,
				methodDef
		);
		final List<ClassDef> classdef = new ArrayList<>();
		classdef.add(cD);

		Program program = new Program(classdef, null);
		assertParses(tokens, program);
	}


	@Test
	public void testAClassDefWithConstructorAndInstanceVariableAndAGetterMethod(){

    	/*
    	Class ClassFoo{
			private int x;
			CLassFoo()
				x = 4;
			public int getX()
				return x;
    	}
    	

		final Token[] tokens = {new ClassToken(),
				new VariableToken("ClassFoo"),
				new LeftCurlyToken(),
				new PrivateToken(),
				new IntToken(),
				new VariableToken("x"),
				new SemiToken(),
				new VariableToken("ClassFoo"),
				new LeftParenToken(),
				new RightParenToken(),
				new VariableToken("x"),
				new EqualToken(),
				new NumberToken(4),
				new SemiToken(),
				new PublicToken(),
				new IntToken(),
				new VariableToken("getX"),
				new LeftParenToken(),
				new RightParenToken(),
				new ReturnToken(),
				new VariableToken("x"),
				new SemiToken(),
				new RightCurlyToken()
		};

		final List<MethodDef> methodDef = new ArrayList<>();
		final VarDec [] varDec = {};
		methodDef.add(new MethodDef(new PublicAccess(),new Type(new IntType()), new MethodName("getX"), varDec, new ReturnExpStatement(new VariableExp("x"))));
		final List<InstanceDec> insDec = new ArrayList<>();
		insDec.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("x"))));
		final Statement st = new VarAssignment(new Variable("x"), new NumberExp(4));
		final ClassDef classdef = new ClassDef(new ClassName("ClassFoo"),

				new Constructor(varDec,
						new VarAssignment(new Variable("x"),
								new NumberExp(4))),
				insDec,
				st,
				methodDef);


		Program program = new Program(classdef, null);
		assertParses(tokens, program);

	}

	@Test
	public void testAClassDefWithConstructorAndInstanceVariableAndASetterMethod(){
    	/*
    	Class ClassFoo{
			private int x;
			CLassFoo()
				x = 0;
			public void setX(int setXToThis)
				 x = setXToThis;
    	}
    	
		final Token[] tokens = {new ClassToken(),
				new VariableToken("ClassFoo"),
				new LeftCurlyToken(),
				new PrivateToken(),
				new IntToken(),
				new VariableToken("x"),
				new SemiToken(),
				new VariableToken("ClassFoo"),
				new LeftParenToken(),
				new RightParenToken(),
				new VariableToken("x"),
				new EqualToken(),
				new NumberToken(0),
				new SemiToken(),
				new PublicToken(),
				new VoidToken(),
				new VariableToken("setX"),
				new LeftParenToken(),
				new IntToken(),
				new VariableToken("setXToThis"),
				new RightParenToken(),
				new VariableToken("x"),
				new EqualToken(),
				new VariableToken("setXToThis"),
				new SemiToken(),
				new RightCurlyToken()
		};

		final List<MethodDef> methodDef = new ArrayList<>();
		final VarDec [] varDec = {};
		final VarDec [] varDec2 = {new VarDec(new IntType(), new Variable("setXToThis"))};
		methodDef.add(new MethodDef(new PublicAccess(),
				new IntType(),
				new  MethodName("setX"),
				varDec,
				new VarAssignment(new Variable("x"), new VariableExp("setXToThis"))));
		final List<InstanceDec> insDec = new ArrayList<>();
		insDec.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("x"))));

		final Statement st = new VarAssignment(new Variable("x"), new NumberExp(0));
		final ClassDef [] classdef = {new ClassDef(new ClassName("ClassFoo"),

				new Constructor(varDec,
						new VarAssignment(new Variable("x"),
								new NumberExp(0))),
				insDec,
				st,
				methodDef)};


		Program program = new Program(classdef, null);
		assertParses(tokens, program);
	}


	@Test
	public void testAClassDefWithConstructorAndInstanceVariableAndSetterGetterMethods(){

    	/*
    	Class ClassFoo{
			private int x;
			CLassFoo()
				x = 0;
			public void setX(int setXToThis)
				 x = setXToThis;
			public int getX()
				return x;
    	}
    	
		final Token[] tokens = {new ClassToken(),
				new VariableToken("ClassFoo"),
				new LeftCurlyToken(),
				new PrivateToken(),
				new IntToken(),
				new VariableToken("x"),
				new SemiToken(),
				new VariableToken("ClassFoo"),
				new LeftParenToken(),
				new RightParenToken(),
				new VariableToken("x"),
				new EqualToken(),
				new NumberToken(0),
				new SemiToken(),
				new PublicToken(),
				new VoidToken(),
				new VariableToken("setX"),
				new LeftParenToken(),
				new IntToken(),
				new VariableToken("setXToThis"),
				new RightParenToken(),
				new VariableToken("x"),
				new EqualToken(),
				new VariableToken("setXToThis"),
				new SemiToken(),

				new PublicToken(),
				new IntToken(),
				new VariableToken("getX"),
				new LeftParenToken(),
				new RightParenToken(),
				new ReturnToken(),
				new VariableToken("x"),
				new SemiToken(),
				new RightCurlyToken()
		};
		final List<InstanceDec> insDec = new ArrayList<>();
		insDec.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("x"))));
		final VarDec [] varDec = {};
		final VarDec [] varDec2 = {new VarDec(new IntType(), new Variable("setXToThis"))};
		final List<MethodDef> methodDef = new ArrayList<>();

		final Statement st = new VarAssignment(new Variable("x"), new NumberExp(0));
		methodDef.add(new MethodDef(new PublicAccess(),
				new Type(new VoidType()),
				new MethodName("setX"),
				varDec2,
				new VarAssignment(new Variable("x"), new VariableExp("setXToThis"))));

		methodDef.add(new MethodDef(new PublicAccess(),
				new Type(new IntType()),
				new MethodName("getX"),
				varDec,
				new ReturnExpStatement(new VariableExp("x"))));
		final ClassDef [] classdef = {new ClassDef(new ClassName("ClassFoo"),

				new Constructor(varDec,
						new VarAssignment(new Variable("x"),
								new NumberExp(0))),
				insDec,
				st,
				methodDef)};

		Program program = new Program(classdef, null);
		assertParses(tokens, program);
	}

}

*****/

// package j2script;
// import java.util.List;
// import java.util.*;
// import static org.junit.Assert.assertTrue;
// import static org.junit.Assert.assertArrayEquals;
// import java.util.Arrays;
// import static org.junit.Assert.assertTrue;
// import static org.junit.Assert.assertEquals;


// import j2script.*;
// import j2script.expressions.*;
// import j2script.operators.*;
// import j2script.tokens.*;
// import org.junit.Test;
// import org.junit.Rule;
// import org.junit.rules.ExpectedException;

// public class ParserTest {
//   public void assertParses(final Token[] tokens, final Exp expected) {
//         final Parser parser = new Parser(tokens);
//         try {
//             final Exp received = parser.parseExp();
//             assertTrue("Expected parse failure; got: " + received, expected != null);
//             assertEquals(expected, received);
//         } catch (final ParserException e) {
//             assertTrue(("Unexpected parse failure for " + Arrays.toString(tokens) +
//             			": " + e.getMessage()), expected == null);
//         }
//     }

//     @Test
//     public void testIntegerParsing() {
//         assertParses(new Token[]{ new NumberToken(123) }, new NumberExp(123));
//     }

//     @Test
//     public void testVariableParsing() {
//         assertParses(new Token[]{ new VariableToken("foo") }, new VariableExp("foo"));
//     }

// 	@Test
// 	public void testParsesIf() {
// 	    final Token[] tokens = { new IfToken(),
// 	                                 new LeftParenToken(),
// 	                                 new NumberToken(4),
// 	                                 new RightParenToken(),
// 	                                 new LeftCurlyToken(),
// 	                                 new NumberToken(2),
// 	                                 new RightCurlyToken(),
// 	                                 new ElseToken(),
// 	                                 new LeftCurlyToken(),
// 	                                 new NumberToken(0),
// 	                                 new RightCurlyToken() };
// 	    final Exp expected = new IfExp(new NumberExp(4),
// 	                                       new NumberExp(2),
// 	                                       new NumberExp(0));
// 	    assertParses(tokens, expected);
// 	}

//     @Test
//     public void testBinaryParsing() {
//     	final Token[] tokens = { new NumberToken(4),
//     							 new AddToken(),
//     							 new NumberToken(4)
//     							 };

//     	final Exp expected = new BinaryExp(new NumberExp(4), new PlusOp(), new NumberExp(4));
//     	assertParses(tokens, expected);
//     }


//     @Test /*passes*/
//         public void testParserExceptionWithEmptyTokens() {
//     	final Token[] tokens = {};
//     	assertParses(tokens, null);
//     }


// }