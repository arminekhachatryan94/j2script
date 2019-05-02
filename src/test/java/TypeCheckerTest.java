package j2script;

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
import java.util.List;
import j2script.TypeChecker;
import java.util.ArrayList;

public class TypeCheckerTest {

	//assigning types
    @Test//(expected = TypeErrorException.class) 
    public void testClassWithWrongVarDecStatementWithIntType() throws TypeErrorException {
    	/*
	 class Foo{
            Foo()
                int x = "test";  <--  int to string  
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>(); //empty
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
    	new VarDec(new ClassType(new ClassName("Foo")), new Variable("y")), 
    	new ClassExp(new ClassName("Foo"), classExp)
    	);

    List<VarDec> varDec = new ArrayList<>(); //empty
   	classDef.add(new ClassDef(
    	new ClassName("Foo"), 
    	new Constructor(
    		varDec, 
    		new VarDecAssignment(
    			new VarDec(
    				new IntType(), 
    				new Variable("x")
    				), 
    			//new StringExp("test")
    			new VariableExp(new Variable("test"))
    			)
    		), 
    	varDec, methodDefs));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
    	
    }

    @Test//(expected = TypeErrorException.class) 
    public void testClassWithWrongVarDecStatementWithStringType() throws TypeErrorException {
    	/*
	 class Foo{
            Foo()
                String x = 0;  <--   
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>(); //empty
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
    	new VarDec(new ClassType(new ClassName("Foo")), new Variable("y")), 
    	new ClassExp(new ClassName("Foo"), classExp)
    	);

    List<VarDec> varDec = new ArrayList<>(); //empty
   	classDef.add(new ClassDef(
    	new ClassName("Foo"), 
    	new Constructor(
    		varDec, 
    		new VarDecAssignment(
    			new VarDec(
    				new StringType(), 
    				new Variable("x")
    				), 
    			new NumberExp(0)
    			)
    		), 
    	varDec, methodDefs));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
    	
    }

    @Test//(expected = TypeErrorException.class) 
    public void testClassWithWrongVarDecStatementWithBooleanType() throws TypeErrorException{
    	/*
	 class Foo{
            Foo()
                boolean x = 0;  <--   
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>(); //empty
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
    	new VarDec(new ClassType(new ClassName("Foo")), new Variable("y")), 
    	new ClassExp(new ClassName("Foo"), classExp)
    	);

    List<VarDec> varDec = new ArrayList<>(); //empty
   	classDef.add(new ClassDef(
    	new ClassName("Foo"), 
    	new Constructor(
    		varDec, 
    		new VarDecAssignment(
    			new VarDec(
    				new BooleanType(), 
    				new Variable("x")
    				), 
    			new NumberExp(0)
    			)
    		), 
    	varDec, methodDefs));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
    	
    }

    @Test//(expected = TypeErrorException.class) 
    public void testClassWithWrongVarDecStatementWithClassType() throws TypeErrorException{
    	/*
	 class Foo{
            Foo()
                int x = 0;    
        }
      class Boo{
            Boo()
                int x = 0;  
       }
        Foo y = Boo(); <--

    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>(); //empty
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
    	new VarDec(new ClassType(new ClassName("Foo")), new Variable("y")), 
    	new ClassExp(new ClassName("Boo"), classExp)
    	);

    List<VarDec> varDec = new ArrayList<>(); //empty
   	classDef.add(new ClassDef(
    	new ClassName("Foo"), 
    	new Constructor(
    		varDec, 
    		new VarDecAssignment(
    			new VarDec(
    				new IntType(), 
    				new Variable("x")
    				), 
    			new NumberExp(0)
    			)
    		), 
    	varDec, methodDefs));
   	classDef.add(new ClassDef(
    	new ClassName("Boo"), 
    	new Constructor(
    		varDec, 
    		new VarDecAssignment(
    			new VarDec(
    				new IntType(), 
    				new Variable("x")
    				), 
    			new NumberExp(0)
    			)
    		), 
    	varDec, methodDefs));
    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
    	
    }

    @Test//(expected = TypeErrorException.class) 
    public void testClassWithInstanceVarAssignmentConstructor() throws TypeErrorException{
    /** class foo{
    int x;
    foo(boolean y) 
        x = y ; <--- int to boolean 
    }

    Foo y = Foo(true);
**/
	    List<ClassDef> classDef = new ArrayList<>();
	    List<MethodDef> methodDefs = new ArrayList<>(); //empty
	    List<Exp> classExp = new ArrayList<>();
	    classExp.add(new BoolExp(true));

	    Statement statement = new VarDecAssignment(
	    	new VarDec(new ClassType(new ClassName("Foo")), new Variable("y")), 
	    	new ClassExp(new ClassName("Foo"), classExp)
	    	);

	    List<VarDec> varDec = new ArrayList<>(); //empty
	    varDec.add(new VarDec(new BooleanType(), new Variable("y")));
	    List<VarDec> varDecNonEmpty = new ArrayList<>();
	    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
	   	classDef.add(new ClassDef(
	    	new ClassName("Foo"), 
	    	new Constructor(
	    		varDec, 
	    		new VarDecAssignment(
	    			new VarDec(
	    				new IntType(), 
	    				new Variable("x")
	    				), 
	    			new VariableExp(new Variable("y"))
	    			)
	    		), 
	    	varDecNonEmpty, methodDefs));

	    final Program program = new Program(classDef, statement);
	    TypeChecker.typecheckProgram(program);
	}

    //Assigning types tests end

	//return types
    @Test// (expected = TypeErrorException.class) 
    public void testClassWithWrongVarDecStatementWithVoidTypeReturn() throws TypeErrorException {
    	/*
	 class Foo{
            Foo()
                int x = 0;   
            private void methodA() {
				return 1; <-- no return, its void
            }
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
    	new VarDec(new ClassType(new ClassName("Foo")), new Variable("y")), 
    	new ClassExp(new ClassName("Foo"), classExp)
    	);

    List<VarDec> varDec = new ArrayList<>(); //empty
    methodDefs.add(new MethodDef(new PrivateAccess(), new VoidType(), new MethodName("methodA"), varDec, new ReturnExpStatement(new NumberExp(1))));
   	classDef.add(new ClassDef(
    	new ClassName("Foo"), 
    	new Constructor(
    		varDec, 
    		new VarDecAssignment(
    			new VarDec(
    				new IntType(), 
    				new Variable("x")
    				), 
    			new NumberExp(0)
    			)
    		), 
    	varDec, methodDefs));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
    	
    }

    @Test//(expected = TypeErrorException.class) 
    public void testClassWithWrongVarDecStatementWithIntTypeReturn() throws TypeErrorException {
    	/*
	 class Foo{
            Foo()
                int x = 0;   
            private int methodA() {
				return "what"; <-- return int 
            }
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
    	new VarDec(new ClassType(new ClassName("Foo")), new Variable("y")), 
    	new ClassExp(new ClassName("Foo"), classExp)
    	);

    List<VarDec> varDec = new ArrayList<>(); //empty
    methodDefs.add(new MethodDef(new PrivateAccess(), new IntType(), new MethodName("methodA"), varDec, new ReturnExpStatement(new VariableExp(new Variable("what")))));
   	classDef.add(new ClassDef(
    	new ClassName("Foo"), 
    	new Constructor(
    		varDec, 
    		new VarDecAssignment(
    			new VarDec(
    				new IntType(), 
    				new Variable("x")
    				), 
    			new NumberExp(0)
    			)
    		), 
    	varDec, methodDefs));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
    	
    }

    @Test//(expected = TypeErrorException.class) 
    public void testClassWithWrongVarDecStatementWithNoReturn() throws TypeErrorException {
    	/*
	 class Foo{
            Foo()
                int x = 0;   
            private int methodA() {
				int y = 0; <-- should return
            }
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
    	new VarDec(new ClassType(new ClassName("Foo")), new Variable("y")), 
    	new ClassExp(new ClassName("Foo"), classExp)
    	);

    List<VarDec> varDec = new ArrayList<>(); //empty
    methodDefs.add(new MethodDef(new PrivateAccess(), new IntType(), new MethodName("methodA"), varDec, new VarDecAssignment(new VarDec(
    				new IntType(), 
    				new Variable("y")
    				), 
    			new NumberExp(0)
    )));
   	classDef.add(new ClassDef(
    	new ClassName("Foo"), 
    	new Constructor(
    		varDec, 
    		new VarDecAssignment(
    			new VarDec(
    				new IntType(), 
    				new Variable("x")
    				), 
    			new NumberExp(0)
    			)
    		), 
    	varDec, methodDefs));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
    	
    }

    // return type tests end
    

	@Test//(expected = TypeErrorException.class) 
	    public void testClassWithInstanceVarAssignmentNotIncludedInStatement() throws TypeErrorException {
	    /** class foo{
	    int x;
	    foo(int y) 
	        x = y ; 
	    }

	    Foo y = Foo(); <-- should have paramter
	**/
	    List<ClassDef> classDef = new ArrayList<>();
	    List<MethodDef> methodDefs = new ArrayList<>(); //empty
	    List<Exp> classExp = new ArrayList<>();

	    Statement statement = new VarDecAssignment(
	    	new VarDec(new ClassType(new ClassName("Foo")), new Variable("y")), 
	    	new ClassExp(new ClassName("Foo"), classExp)
	    	);

	    List<VarDec> varDec = new ArrayList<>(); //empty
	    varDec.add(new VarDec(new BooleanType(), new Variable("y")));
	    List<VarDec> varDecNonEmpty = new ArrayList<>();
	    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
	   	classDef.add(new ClassDef(
	    	new ClassName("Foo"), 
	    	new Constructor(
	    		varDec, 
	    		new VarDecAssignment(
	    			new VarDec(
	    				new IntType(), 
	    				new Variable("x")
	    				), 
	    			new VariableExp(new Variable("y"))
	    			)
	    		), 
	    	varDecNonEmpty, methodDefs));

	    final Program program = new Program(classDef, statement);
	    TypeChecker.typecheckProgram(program);
	}

	@Test//(expected = TypeErrorException.class) 
	    public void testClassWithInstanceVarAssignmentNotIncludedInStatementReverse() throws TypeErrorException{
	    /** class foo{
	    int x;
	    foo() 
	        x = 0 ; 
	    }

	    Foo y = Foo(1); <-- should not have paramter
	**/
	    List<ClassDef> classDef = new ArrayList<>();
	    List<MethodDef> methodDefs = new ArrayList<>(); //empty
	    List<Exp> classExp = new ArrayList<>();
	    classExp.add(new NumberExp(1));

	    Statement statement = new VarDecAssignment(
	    	new VarDec(new ClassType(new ClassName("Foo")), new Variable("y")), 
	    	new ClassExp(new ClassName("Foo"), classExp)
	    	);

	    List<VarDec> varDec = new ArrayList<>(); //empty
	    List<VarDec> varDecNonEmpty = new ArrayList<>();
	    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
	   	classDef.add(new ClassDef(
	    	new ClassName("Foo"), 
	    	new Constructor(
	    		varDec, 
	    		new VarDecAssignment(
	    			new VarDec(
	    				new IntType(), 
	    				new Variable("x")
	    				), 
	    			new NumberExp(0)
	    			)
	    		), 
	    	varDecNonEmpty, methodDefs));

	    final Program program = new Program(classDef, statement);
	    TypeChecker.typecheckProgram(program);
	}

	@Test//(expected = TypeErrorException.class) 
	    public void testClassWithInstanceVarAssignmentWithoutVarDec() throws TypeErrorException {
	    /** class foo{
	    
	    foo() 
	        x = 0 ; <-- not declared
	    }

	    Foo y = Foo();
	**/
	    List<ClassDef> classDef = new ArrayList<>();
	    List<MethodDef> methodDefs = new ArrayList<>(); //empty
	    List<Exp> classExp = new ArrayList<>();

	    Statement statement = new VarDecAssignment(
	    	new VarDec(new ClassType(new ClassName("Foo")), new Variable("y")), 
	    	new ClassExp(new ClassName("Foo"), classExp)
	    	);

	    List<VarDec> varDec = new ArrayList<>(); //empty
	   	classDef.add(new ClassDef(
	    	new ClassName("Foo"), 
	    	new Constructor(
	    		varDec, 
	    		new VarDecAssignment(
	    			new VarDec(
	    				new IntType(), 
	    				new Variable("x")
	    				), 
	    			new NumberExp(0)
	    			)
	    		), 
	    	varDec, methodDefs));

	    final Program program = new Program(classDef, statement);
	    TypeChecker.typecheckProgram(program);
	}

	@Test//(expected = TypeErrorException.class) 
	    public void testClassWithInstanceVarAssignmentWithoutVarDecInMethodCall() throws TypeErrorException{
	    /** class foo{
	    int x;
	    foo() 
	        x = 0 ; 
	    }

	    Foo y = Foo(); 
	**/
	    List<ClassDef> classDef = new ArrayList<>();
	    List<MethodDef> methodDefs = new ArrayList<>(); //empty
	    List<Exp> classExp = new ArrayList<>();
	    classExp.add(new NumberExp(0));

	    Statement statement = new VarDecAssignment(
	    	new VarDec(new ClassType(new ClassName("Foo")), new Variable("y")), 
	    	new ClassExp(new ClassName("Foo"), classExp)
	    	);

	    List<VarDec> varDec = new ArrayList<>(); //empty
	    List<VarDec> varDecNonEmpty = new ArrayList<>();
	    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
	   	classDef.add(new ClassDef(
	    	new ClassName("Foo"), 
	    	new Constructor(
	    		varDec, 
	    		new VarDecAssignment(
	    			new VarDec(
	    				new IntType(), 
	    				new Variable("x")
	    				), 
	    			new NumberExp(0)
	    			)
	    		), 
	    	varDecNonEmpty, methodDefs));

	    final Program program = new Program(classDef, statement);
	    TypeChecker.typecheckProgram(program);
	}
	
}