package j2script;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import j2script.*;
import j2script.tokens.*;
import j2script.access.*;
import j2script.declarations.*;
import j2script.expressions.*;
import j2script.names.*;
import j2script.operators.*;
import j2script.statements.*;
import j2script.types.*;
import j2script.ParserException;

public class ParserTest {
	public void assertParses(final List<Token> tokens, final Program expected) {
		final Parser parser = new Parser(tokens);
		try {
			final Program received = parser.parseMe();
			assertTrue("Expected parse failure; got: " + received,
					expected != null);
			assertEquals(expected, received);
		} catch (final ParserException e) {
			assertTrue(("Unexpected parse failure for " +
							tokens.toString() +
							": " + e.getMessage()),
					expected == null);
		}
	}

	@Test
	public void testComplexAlgebraicExpressionStatement(){ 
        //int x = 5 + ((1+2) * 4);

		final ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new IntToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new EqualToken());
        tokens.add(new NumberToken(5));
        tokens.add(new AddToken());
        tokens.add(new LeftParenToken());
        tokens.add(new LeftParenToken());
        tokens.add(new NumberToken(1));
        tokens.add(new AddToken());
        tokens.add(new NumberToken(2));
        tokens.add(new RightParenToken());
        tokens.add(new MultiplyToken());
        tokens.add(new NumberToken(4));
        tokens.add(new RightParenToken());
        tokens.add(new SemiToken());

		final Statement st = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")),
				new BinopExp(new NumberExp(5),
						new PlusOp(),
						new BinopExp(
                            (new BinopExp(
                                new NumberExp(1),
								new PlusOp(),
								new NumberExp(2))
                            ),
                            new MultOp(),
                            new NumberExp(4)
                        )
                )
		);

		Program program = new Program(null, st);
		assertParses(tokens, program);
	}

	@Test
	public void testArithmeticPrecedenceStatement() { 
        // x = 1 - 2 / 3;

		final ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new VariableToken("x"));
        tokens.add(new EqualToken());
        tokens.add(new NumberToken(1));
        tokens.add(new MinusToken());
        tokens.add(new NumberToken(2));
        tokens.add(new DivToken());
        tokens.add(new NumberToken(3));
        tokens.add(new SemiToken());

		final Statement st = new VarAssignment(
            new Variable("x"),
            new BinopExp(
                new NumberExp(1),
                new MinusOp(),
                new BinopExp(
                    new NumberExp(2),
                    new DivOp(),
                    new NumberExp(3)
                )
            )
        );

		Program program = new Program(null, st);
		assertParses(tokens, program);
	}

	@Test
	public void testArithmeticPrecedenceWithParensStatement() {
        // x= (1 - 2) / 3;

		final ArrayList<Token> tokens = new ArrayList<>(); 
        tokens.add(new VariableToken("x"));
        tokens.add(new EqualToken());
        tokens.add(new LeftParenToken());
        tokens.add(new NumberToken(1));
        tokens.add(new MinusToken());
        tokens.add(new NumberToken(2));
        tokens.add(new RightParenToken());
        tokens.add(new DivToken());
        tokens.add(new NumberToken(3));
        tokens.add(new SemiToken());

		final Statement st = new VarAssignment(
            new Variable("x"), 
            new BinopExp(
                new BinopExp(
                    new NumberExp(1),
				    new MinusOp(),
				    new NumberExp(2)
                ),
				new DivOp(),
				new NumberExp(3)
            )
        );


		Program program = new Program(null, st);
		assertParses(tokens, program);
	}

	@Test
	public void testArithmeticLeftAssociativeStatement() {
        //x = 1 + 2 - 3;

		final ArrayList<Token> tokens = new ArrayList<>(); 
        tokens.add(new VariableToken("x"));
        tokens.add(new EqualToken());
        tokens.add(new NumberToken(1));
        tokens.add(new AddToken());
        tokens.add(new NumberToken(2));
        tokens.add(new MinusToken());
        tokens.add(new NumberToken(3));
        tokens.add(new SemiToken());

		final Statement st = new VarAssignment(
            new Variable("x"), 
            new BinopExp(
                new BinopExp(
                    new NumberExp(1),
				    new PlusOp(),
				    new NumberExp(2)
                ),
				new MinusOp(),
				new NumberExp(3)
            )
        );

		Program program = new Program(null, st);
		assertParses(tokens, program);
	}

	@Test
	public void testIfStatementParsing() { 
        /* 
            if(1) 
                foo = 2; 
            else 
                foo = 3;
        */

		final ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new IfToken());
        tokens.add(new LeftParenToken());
        tokens.add(new NumberToken(1));
        tokens.add(new RightParenToken());
        tokens.add(new VariableToken("foo"));
        tokens.add(new EqualToken());
        tokens.add(new NumberToken(2));
        tokens.add(new SemiToken());
        tokens.add(new ElseToken());
        tokens.add(new VariableToken("foo"));
        tokens.add(new EqualToken());
        tokens.add(new NumberToken(3));
        tokens.add(new SemiToken());

		final Statement st = new IfStatement(
            new NumberExp(1),
            new VarAssignment(new Variable("foo"), new NumberExp(2)),
            new VarAssignment(new Variable("foo"), new NumberExp(3))
        );

		Program program = new Program(null, st);
		assertParses(tokens, program);
	}

	@Test
	public void testIfStatementWithABlockParsing() { 
        /*
            if(1) {
                int foo = 2; 
                println(foo);
            } 
            else 
                int foo = 3;
        */
		final ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new IntToken());
        tokens.add(new IfToken());
        tokens.add(new LeftParenToken());
        tokens.add(new NumberToken(1));
        tokens.add(new RightParenToken());
        tokens.add(new LeftCurlyToken());
        tokens.add(new IntToken());
        tokens.add(new VariableToken("foo"));
        tokens.add(new EqualToken());
        tokens.add(new NumberToken(2));
        tokens.add(new SemiToken());
        tokens.add(new PrintToken());
        tokens.add(new LeftParenToken());
        tokens.add(new IntToken());
        tokens.add(new VariableToken("foo"));
        tokens.add(new RightParenToken());
        tokens.add(new SemiToken());
        tokens.add(new RightCurlyToken());
        tokens.add(new ElseToken());
        tokens.add(new VariableToken("foo"));
        tokens.add(new EqualToken());
        tokens.add(new NumberToken(3));
        tokens.add(new SemiToken());

		final List<Statement> statements = new ArrayList<>();
		statements.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("foo")), new NumberExp(2)));
		statements.add(new PrintStatement(new VariableExp(new Variable("foo"))));

		final Statement st = new IfStatement(
            new NumberExp(1),
            new Block(statements),
            new VarDecAssignment(new VarDec(new IntType(), new Variable("foo")), new NumberExp(3))
        );

		Program program = new Program(null, st);
		assertParses(tokens, program);
	}

	@Test
	public void testWhileStatementParsing() { 
        // while(true) println(5);
		final ArrayList<Token> tokens = new ArrayList<>(); 
        tokens.add(new WhileToken());
        tokens.add(new LeftParenToken());
        tokens.add(new TrueToken());
        tokens.add(new RightParenToken());
        tokens.add(new PrintToken());
        tokens.add(new LeftParenToken());
        tokens.add(new NumberToken(5));
        tokens.add(new RightParenToken());
        tokens.add(new SemiToken());

		final Statement stm = new WhileStatement(new BoolExp(true), new PrintStatement(new NumberExp(5)));
		Program program = new Program(null, stm);
		assertParses(tokens, program);
	}

	@Test
	public void testWhileStatmentWithIfElseStatementParsing() { 
        /*
        while(2) { 
            if(2) 
                break; 
            else 
                println(7);
        }
        */
		final ArrayList<Token> tokens = new ArrayList<>(); 
        tokens.add(new WhileToken());
        tokens.add(new LeftParenToken());
        tokens.add(new NumberToken(2));
        tokens.add(new RightParenToken());
        tokens.add(new LeftCurlyToken());
        tokens.add(new IfToken());
        tokens.add(new LeftParenToken());
        tokens.add(new NumberToken(2));
        tokens.add(new RightParenToken());
        tokens.add(new BreakToken());
        tokens.add(new SemiToken());
        tokens.add(new ElseToken());
        tokens.add(new PrintToken());
        tokens.add(new LeftParenToken());
        tokens.add(new NumberToken(7));
        tokens.add(new RightParenToken());
        tokens.add(new SemiToken());
        tokens.add(new RightCurlyToken());

		final Statement stm = new WhileStatement(
                new NumberExp(2), 
                new IfStatement(
                    new NumberExp(2),
                    new BreakStatement(),
                    new PrintStatement(new NumberExp(7))
                )
        );
		Program program = new Program(null, stm);
		assertParses(tokens, program);
	}

	@Test
	public void testBlockStatmentParsing() { 
        // { int x = 0; println(x); }
		final ArrayList<Token> tokens = new ArrayList<>(); 
        tokens.add(new LeftCurlyToken());
        tokens.add(new IntToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new EqualToken());
        tokens.add(new NumberToken(0));
        tokens.add(new SemiToken());
        tokens.add(new PrintToken());
        tokens.add(new LeftParenToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new RightParenToken());
        tokens.add(new SemiToken());
        tokens.add(new RightCurlyToken());

		List<Statement> statements = new ArrayList<>();
		statements.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0)));
        statements.add(new PrintStatement(new VariableExp(new Variable("x"))));

		final Statement stm = new Block(statements);
		Program program = new Program(null, stm);
		assertParses(tokens, program);
	}


	@Test
	public void testAClassDefWithConstructorAndInstanceVariable() {
    	/*
    	class ClassFoo<> {
			int x;
			constructor(int parameterOne)
				x = parameterOne;
    	}
    	*/

		final ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new ClassToken());
        tokens.add(new VariableToken("ClassFoo"));
        tokens.add(new LessThanToken());
        tokens.add(new GreaterThanToken());
        tokens.add(new LeftCurlyToken());
        tokens.add(new IntToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new SemiToken());
        tokens.add(new ConstructorToken());
        tokens.add(new LeftParenToken());
        tokens.add(new IntToken());
        tokens.add(new VariableToken("parameterOne"));
        tokens.add(new RightParenToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new EqualToken());
        tokens.add(new VariableToken("parameterOne"));
        tokens.add(new SemiToken());
        tokens.add(new RightCurlyToken());
		
		final List<VarDec> insDec = new ArrayList<>();
		insDec.add(new VarDec(new IntType(), new Variable("x")));

		final ArrayList<VarDec> varDec = new ArrayList<>(); 
        varDec.add(new VarDec(new IntType(), new Variable("parameterOne")));

		final List<MethodDef> methodDef = new ArrayList<>();

		final Statement st = new VarAssignment(new Variable("x"), new VariableExp(new Variable("parameterOne")));
        
		final ClassDef cd = new ClassDef(new ClassName("ClassFoo"), new Constructor(varDec, st), null, insDec, methodDef, new ArrayList());

		final List<ClassDef> classDef = new ArrayList<>();
		classDef.add(cd);

		Program program = new Program(classDef, null);
		assertParses(tokens, program);
	}

	@Test
	public void testAClassDefWithConstructorAndInstanceVariableAndAVoidMethod() {
    	/*
    	Class ClassFoo<> {
			int x;
			constructor(int parameterOne)
				x = parameterOne;
			private void methodOne(int four)
				four = 4 + x;
    	}
    	*/

		final ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new ClassToken());
        tokens.add(new VariableToken("ClassFoo"));
        tokens.add(new LessThanToken());
        tokens.add(new GreaterThanToken());
        tokens.add(new LeftCurlyToken());
        tokens.add(new IntToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new SemiToken());
        tokens.add(new ConstructorToken());
        tokens.add(new LeftParenToken());
        tokens.add(new IntToken());
        tokens.add(new VariableToken("parameterOne"));
        tokens.add(new RightParenToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new EqualToken());
        tokens.add(new VariableToken("parameterOne"));
        tokens.add(new SemiToken());
        tokens.add(new PrivateToken());
        tokens.add(new VoidToken());
        tokens.add(new VariableToken("methodOne"));
        tokens.add(new LeftParenToken());
        tokens.add(new IntToken());
        tokens.add(new VariableToken("four"));
        tokens.add(new RightParenToken());
        tokens.add(new VariableToken("four"));
        tokens.add(new EqualToken());
        tokens.add(new NumberToken(4));
        tokens.add(new AddToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new SemiToken());
        tokens.add(new RightCurlyToken());

        final List<VarDec> varDec = new ArrayList<>();
        varDec.add(new VarDec(new IntType(), new Variable("parameterOne")));

		final List<MethodDef> methodDef = new ArrayList<>();
        methodDef.add(
            new MethodDef(
                new PrivateAccess(),
                new VoidType(),
                new MethodName("methodOne"),
                varDec,
                new VarAssignment(new Variable("four"), new BinopExp(new NumberExp(4), new PlusOp(), new VariableExp(new Variable("x"))))
            )
        );

		final List<VarDec> insDec = new ArrayList<>();
		insDec.add(new VarDec(new IntType(), new Variable("x")));

		final Statement st = new VarAssignment(new Variable("x"), new VariableExp(new Variable("parameterOne")));

		final ClassDef cD = new ClassDef(
            new ClassName("ClassFoo"),
            new Constructor(
                varDec,
                st
            ),
            null,
            insDec,
            methodDef,
            new ArrayList<>()
		);

		final List<ClassDef> classdef = new ArrayList<>();
		classdef.add(cD);

		Program program = new Program(classdef, null);
		assertParses(tokens, program);
	}


	@Test
	public void testAClassDefWithConstructorAndInstanceVariableAndAGetterMethod() {
    	/*
    	class ClassFoo<>{
			int x;
			constructor()
				x = 4;
			public int getX()
				return x;
    	}
    	*/

		final ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new ClassToken());
        tokens.add(new VariableToken("ClassFoo"));
        tokens.add(new LessThanToken());
        tokens.add(new GreaterThanToken());
        tokens.add(new LeftCurlyToken());
        tokens.add(new IntToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new SemiToken());
        tokens.add(new ConstructorToken());
        tokens.add(new LeftParenToken());
        tokens.add(new RightParenToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new EqualToken());
        tokens.add(new NumberToken(4));
        tokens.add(new SemiToken());
        tokens.add(new PublicToken());
        tokens.add(new IntToken());
        tokens.add(new VariableToken("getX"));
        tokens.add(new LeftParenToken());
        tokens.add(new RightParenToken());
        tokens.add(new ReturnToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new SemiToken());
        tokens.add(new RightCurlyToken());

		final List<MethodDef> methodDef = new ArrayList<>();
		methodDef.add(new MethodDef(
            new PublicAccess(), 
            new IntType(), 
            new MethodName("getX"), 
            new ArrayList<>(), 
            new ReturnExpStatement(new VariableExp(new Variable("x")))
        ));

		final List<VarDec> insDec = new ArrayList<>();
		insDec.add(new VarDec(
            new IntType(), 
            new Variable("x")
        ));

		final Statement st = new VarAssignment(new Variable("x"), new NumberExp(4));

		final ClassDef cD = new ClassDef(
            new ClassName("ClassFoo"),
            new Constructor(new ArrayList<>(), st),
            null,
            insDec,
            methodDef,
            new ArrayList<>()
        );

		final List<ClassDef> classdef = new ArrayList<>();
		classdef.add(cD);

		Program program = new Program(classdef, null);
		assertParses(tokens, program);
	}

	@Test
	public void testAClassDefWithConstructorAndInstanceVariableAndASetterMethod() {
    	/*
    	class ClassFoo<> {
			int x;
			constructor()
				x = 0;
			public void setX(int setXToThis)
				 x = setXToThis;
    	}
    	*/
		final ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new ClassToken());
        tokens.add(new VariableToken("ClassFoo"));
        tokens.add(new LessThanToken());
        tokens.add(new GreaterThanToken());
        tokens.add(new LeftCurlyToken());
        tokens.add(new IntToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new SemiToken());
        tokens.add(new ConstructorToken());
        tokens.add(new LeftParenToken());
        tokens.add(new RightParenToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new EqualToken());
        tokens.add(new NumberToken(0));
        tokens.add(new SemiToken());
        tokens.add(new PublicToken());
        tokens.add(new VoidToken());
        tokens.add(new VariableToken("setX"));
        tokens.add(new LeftParenToken());
        tokens.add(new IntToken());
        tokens.add(new VariableToken("setXToThis"));
        tokens.add(new RightParenToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new EqualToken());
        tokens.add(new VariableToken("setXToThis"));
        tokens.add(new SemiToken());
        tokens.add(new RightCurlyToken());

		final List<MethodDef> methodDef = new ArrayList<>();
		final List<VarDec> varDec = new ArrayList<>();
        varDec.add(new VarDec(new IntType(), new Variable("setXToThis")));

		methodDef.add(new MethodDef(
            new PublicAccess(),
            new IntType(),
            new MethodName("setX"),
            varDec,
            new VarAssignment(new Variable("x"), new VariableExp(new Variable("setXToThis")))
        ));

		final List<VarDec> insDec = new ArrayList<>();
		insDec.add(new VarDec(new IntType(), new Variable("x")));

		final Statement st = new VarAssignment(new Variable("x"), new NumberExp(0));

		final List<ClassDef> classDef = new ArrayList<>();
        classDef.add(
            new ClassDef(
                new ClassName("ClassFoo"),
                new Constructor(varDec, st),
                null,
                insDec,
                methodDef,
                new ArrayList<>()
            )
        );


		Program program = new Program(classDef, null);
		assertParses(tokens, program);
	}

	@Test
	public void testAClassDefWithConstructorAndInstanceVariableAndSetterGetterMethods() {
    	/*
    	class ClassFoo<> {
			int x;
			constructor()
				x = 0;
			public void setX(int setXToThis)
				 x = setXToThis;
			public int getX()
				return x;
    	}
    	*/

		final ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new ClassToken());
        tokens.add(new VariableToken("ClassFoo"));
        tokens.add(new LessThanToken());
        tokens.add(new GreaterThanToken());
        tokens.add(new LeftCurlyToken());
        tokens.add(new IntToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new SemiToken());
        tokens.add(new ConstructorToken());
        tokens.add(new LeftParenToken());
        tokens.add(new RightParenToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new EqualToken());
        tokens.add(new NumberToken(0));
        tokens.add(new SemiToken());
        tokens.add(new PublicToken());
        tokens.add(new VoidToken());
        tokens.add(new VariableToken("setX"));
        tokens.add(new LeftParenToken());
        tokens.add(new IntToken());
        tokens.add(new VariableToken("setXToThis"));
        tokens.add(new RightParenToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new EqualToken());
        tokens.add(new VariableToken("setXToThis"));
        tokens.add(new SemiToken());
        tokens.add(new PublicToken());
        tokens.add(new IntToken());
        tokens.add(new VariableToken("getX"));
        tokens.add(new LeftParenToken());
        tokens.add(new RightParenToken());
        tokens.add(new ReturnToken());
        tokens.add(new VariableToken("x"));
        tokens.add(new SemiToken());
        tokens.add(new RightCurlyToken());

		final List<VarDec> insDec = new ArrayList<>();
		insDec.add(new VarDec(new IntType(), new Variable("x")));

		final List<VarDec> varDec = new ArrayList<>();

		final List<VarDec> varDec2 = new ArrayList<>();
        varDec2.add(new VarDec(new IntType(), new Variable("setXToThis")));

		final List<MethodDef> methodDef = new ArrayList<>();
		methodDef.add(new MethodDef(
            new PublicAccess(),
            new VoidType(),
            new MethodName("setX"),
            varDec2,
            new VarAssignment(new Variable("x"), new VariableExp(new Variable("setXToThis")))
        ));

		methodDef.add(new MethodDef(
            new PublicAccess(),
            new IntType(),
            new MethodName("getX"),
            varDec,
            new ReturnExpStatement(new VariableExp(new Variable("x")))
        ));

        final Statement st = new VarAssignment(new Variable("x"), new NumberExp(0));
		final List<ClassDef> classDef = new ArrayList<>();
        classDef.add(new ClassDef(
            new ClassName("ClassFoo"),
            new Constructor(
                varDec,
                st
            ),
            null,
            insDec,
            methodDef,
            new ArrayList<>()
        ));

		Program program = new Program(classDef, null);
		assertParses(tokens, program);
	}
}