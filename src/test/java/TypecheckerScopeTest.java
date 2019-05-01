// package j2script.typechecker;

// import j2script.*;

// import org.junit.Test;

// public class TypecheckerScopeTest {

// public void testConstructorAssignmentsOfIntegerInstanceVariables() {
//     /* class Foo{
//             private int x;
//             Foo()
//                 x = 0; 
            
//         }
//         int x = 0; 
//     */
//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<ClassDef> methodDefs = new ArrayList<>(); //empty

//     VarDec[] varDec = {}; //empty
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("x"))));
//     classDef.add(new ClassDef(  new ClassName("foo"),
                                
//                                 new Constructor(vardec, new VarAssignment(new Variable("x"), new NumberExp(0))),
//                                 instanceVars,
//                                 methodDefs));

//     Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, statment);
//     Typechecker.typecheckProgram(program);
// }

// public void testConstructorAssignmentsOfIntegerInstanceVariablesWithParameter() {
//     /** class foo{
//     private int x;
//     foo(int y) 
//         x = y;
//     }
// **/
//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<ClassDef> methodDefs = new ArrayList<>(); //empty

//     VarDec[] varDec = {new VarDec(new IntType(), new Variable("y"))}; //empty
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("x"))));
//     classDef.add(new ClassDef(  new ClassName("foo"),
                                
//                                 new Constructor(vardec, new VarAssignment(new Variable("x"), new VariableExp("y"))),
//                                 instanceVars,
//                                 methodDefs));
//     //Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, null);
//     Typechecker.typecheckProgram(program);
// }


// public void testConstructorAssignmentsOfIntegerInstanceVariableWithASetterMethod() {
//     /** class foo{
//     private int x;
//     foo(int y) 
//         x = y;
//     public void setX(int z) 
//         x = z;
//     }
// **/
//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<MethodDef> methodDefs = new ArrayList<>();

//     VarDec[] varDec = {new VarDec(new IntType(), new Variable("y"))}; 
//     VarDec[] varDec2 = {new VarDec(new IntType(), new Variable("z"))};
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("x"))));
//     methodDefs.add(new MethodDef(new PublicAccess(), new VoidType(), new MethodName("setX"), varDec2, new VarAssignment(new Variable("x"), new VariableExp("z"))));
//     classDef.add(new ClassDef(  new ClassName("foo"),
                                
//                                 new Constructor(vardec, new VarAssignment(new Variable("x"), new VariableExp("y"))),
//                                 instanceVars,
//                                 methodDefs));
//     //Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, null);
//     Typechecker.typecheckProgram(program);
// }


// public void testConstructorAssignmentsOfStringInstanceVariables() {
//     /* class foo{
//             private String x;
//             foo()
//                 x = "ex"; 
            
//         }
//     */
//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<ClassDef> methodDefs = new ArrayList<>(); //empty

//     VarDec[] varDec = {}; //empty
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new StringType(), new Variable("x"))));
//     classDef.add(new ClassDef(  new ClassName("foo"),
                                
//                                 new Constructor(vardec, new VarAssignment(new Variable("x"), new StringExp("ex"))),
//                                 instanceVars,
//                                 methodDefs));
//     //Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, null);
//     Typechecker.typecheckProgram(program);
// }


// public void testConstructorAssignmentsOfStringInstanceVariablesWithParameter() {
//     /** class foo{
//     private String x;
//     foo(String y) 
//         x = y;
//     }
// **/
//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<ClassDef> methodDefs = new ArrayList<>(); //empty

//     VarDec[] varDec = {new VarDec(new StringType(), new Variable("y"))}; //empty
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new StringType(), new Variable("x"))));
//     classDef.add(new ClassDef(  new ClassName("foo"),
                                
//                                 new Constructor(vardec, new VarAssignment(new Variable("x"), new VariableExp("y"))),
//                                 instanceVars,
//                                 methodDefs));
//     //Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, null);
//     Typechecker.typecheckProgram(program);
// }


// public void testConstructorAssignmentsOfStringInstanceVariableWithASetterMethod() {
//     /** class foo{
//     private String x;
//     foo() 
//         x = "";
//     public void setX(String z) 
//         x = z;
//     }
// **/
//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<MethodDef> methodDefs = new ArrayList<>();

//     VarDec[] varDec = {}; 
//     VarDec[] varDec2 = {new VarDec(new StringType(), new Variable("z"))};
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new StringType(), new Variable("x"))));
//     methodDefs.add(new MethodDef(new PublicAccess(), new VoidType(), new MethodName("setX"), varDec2, new VarAssignment(new Variable("x"), new VariableExp("z"))));
//     classDef.add(new ClassDef(  new ClassName("foo"),
                                
//                                 new Constructor(vardec, new VarAssignment(new Variable("x"), new StringExp(""))),
//                                 instanceVars,
//                                 methodDefs));
//     //Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, null);
//     Typechecker.typecheckProgram(program);
// }


// public void testConstructorAssignmentsOfIntInstanceVariableWithAGetterMethod() {
//     /** class foo{
//     private int x;
//     foo() 
//         x = 4;
//     public int getX() 
//         return x;
//     }
// **/
//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<MethodDef> methodDefs = new ArrayList<>();

//     VarDec[] varDec = {}; 
//     VarDec[] varDec2 = {};
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("x"))));
//     methodDefs.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getX"), varDec2, new ReturnExpStatement(new StringExp("x"))));
//     classDef.add(new ClassDef(  new ClassName("foo"),
                                
//                                 new Constructor(vardec, new VarAssignment(new Variable("x"), new NumberExp(4))),
//                                 instanceVars,
//                                 methodDefs));
//     //Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, null);
//     Typechecker.typecheckProgram(program);
// }


// public void testConstructorAssignmentsOfStringInstanceVariableWithAGetterMethod() {
//     /** class foo{
//     private String x;
//     foo() 
//         x = "joey wants food";
//     public String getX() 
//         return x;
//     }
// **/
//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<MethodDef> methodDefs = new ArrayList<>();

//     VarDec[] varDec = {}; 
//     VarDec[] varDec2 = {};
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new StringType(), new Variable("x"))));
//     methodDefs.add(new MethodDef(new PublicAccess(), new StringType(), new MethodName("getX"), varDec2, new ReturnExpStatement(new StringExp("x"))));
//     classDef.add(new ClassDef(  new ClassName("foo"),
                                
//                                 new Constructor(vardec, new VarAssignment(new Variable("x"), new StringExp("joey wants food"))),
//                                 instanceVars,
//                                 methodDefs));
//     //Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, null);
//     Typechecker.typecheckProgram(program);
// }

// @Test(expected = TypeErrorException.class) 
// public void testConstructorAssignmentsOfStringInstanceVariableWithAGetterMethod() {
//     /** class foo{
//     private String x;
//     foo() 
//         x = "joey wants food";
//     public int getX() ---> should return integer but returns string
//         return x;
//     }
// **/
//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<MethodDef> methodDefs = new ArrayList<>();

//     VarDec[] varDec = {}; 
//     VarDec[] varDec2 = {};
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new StringType(), new Variable("x"))));
//     methodDefs.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getX"), varDec2, new ReturnExpStatement(new StringExp("x"))));
//     classDef.add(new ClassDef(  new ClassName("foo"),
                                
//                                 new Constructor(vardec, new VarAssignment(new Variable("x"), new StringExp("joey wants food"))),
//                                 instanceVars,
//                                 methodDefs));
//     //Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, null);
//     Typechecker.typecheckProgram(program);
// }


// @Test(expected = TypeErrorException.class) 
// public void testAssigningVariables() {
//     /** class foo{
//     private String x;
//     foo() 
//         x = 3; --> wrong assignment
//     public void setX(String z) 
//         x = z;
//     }
//     **/
//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<MethodDef> methodDefs = new ArrayList<>();

//     VarDec[] varDec = {}; 
//     VarDec[] varDec2 = {new VarDec(new StringType(), new Variable("z"))};
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new StringType(), new Variable("x"))));
//     methodDefs.add(new MethodDef(new PublicAccess(), new VoidType(), new MethodName("setX"), varDec2, new VarAssignment(new Variable("x"), new VariableExp("z"))));
//     classDef.add(new ClassDef(  new ClassName("foo"),
                                
//                                 new Constructor(vardec, new VarAssignment(new Variable("x"), new NumberExp(3))),
//                                 instanceVars,
//                                 methodDefs));
//     //Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, null);
//     Typechecker.typecheckProgram(program);
// }

// @Test(expected = TypeErrorException.class) 
// public void testUnDeclaredVariables() {

// /*
//     class Foo{
    
//     Foo() 
//         x =4; ---> never declared
//     }
// */

//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<MethodDef> methodDefs = new ArrayList<>();

//     VarDec[] varDec = {};
    
//     classDef.add(new ClassDef(  new ClassName("foo"),
                                
//                                 new Constructor(vardec, new VarAssignment(new Variable("x"), new NumberExp(4))),
//                                 instanceVars,
//                                 methodDefs));
//     //Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, null);
//     Typechecker.typecheckProgram(program);


// }


// @Test(expected = TypeErrorException.class) 
// public void testUnAssignedVariables() {

// /*
//     class Foo{
//     private int x;
//     private int y;
//     Foo() 
//         x =4; 
//     }
//     public int getY() 
//         return y;  ----> never assigned
// */

//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<MethodDef> methodDefs = new ArrayList<>();

//     VarDec[] varDec = {}; 
//     VarDec[] varDec2 = {};
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("x"))));
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("y"))));
//     methodDefs.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getY"), varDec2, new ReturnExpStatement(new StringExp("y"))));
//     classDef.add(new ClassDef(  new ClassName("foo"),
                                
//                                 new Constructor(vardec, new VarAssignment(new Variable("x"), new NumberExp(4))),
//                                 instanceVars,
//                                 methodDefs));
//     //Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, null);
//     Typechecker.typecheckProgram(program);


// }

// @Test(expected = TypeErrorException.class) 
// public void testScopeOfTheMethodVariable() {

// /*
//     class Foo{
//         int x;
//         Foo(){
//             x = 0;
//             int y = 4;
//         }
//         public void setY(int z)
//             y =z;
//     }
// */


//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<MethodDef> methodDefs = new ArrayList<>();
//     List<Statement> st = new ArrayList<>();

//     st.add(new VarAssignment(new Variable("x"), new NumberExp(0)));
//     st.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("z")), new NumberExp(4)));

//     VarDec[] varDec = {new VarDec(new IntType(), new Variable("y"))}; 
//     VarDec[] varDec2 = {new VarDec(new IntType(), new Variable("z"))};
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("x"))));
//     methodDefs.add(new MethodDef(new PublicAccess(), new VoidType(), new MethodName("setY"), varDec2, new VarAssignment(new Variable("y"), new VariableExp("z"))));
//     classDef.add(new ClassDef(  new ClassName("foo"),
                                
//                                 new Constructor(vardec, new Block(st)),
//                                 instanceVars,
//                                 methodDefs));
//     //Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, null);
//     Typechecker.typecheckProgram(program);

// }

// @Test(expected = TypeErrorException.class) 
// public void testReturnIsCalledForAMethod() {
// /*
//     class Foo{
//     private int x;
//     private int y;
//     Foo() 
//         x =4; 
//     }
//     public int getY() ---> should have a return statement
//         y =4; 
// */

//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<MethodDef> methodDefs = new ArrayList<>();

//     VarDec[] varDec = {}; 
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("x"))));
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("y"))));
//     methodDefs.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getY"), varDec, new VarAssignment(new Variable("y"), new NumberExp(4))));
//     classDef.add(new ClassDef(  new ClassName("foo"),
                                
//                                 new Constructor(vardec, new VarAssignment(new Variable("x"), new NumberExp(4))),
//                                 instanceVars,
//                                 methodDefs));
//     //Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, null);
//     Typechecker.typecheckProgram(program);



// }

// @Test(expected = TypeErrorException.class) 
// public void testBreakStatement() {

// /*
//     class Foo{
    
//     Foo() 
//         break; --? break is only allowed in loops
//     }
// */

//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<MethodDef> methodDefs = new ArrayList<>();

//     VarDec[] varDec = {};
    
//     classDef.add(new ClassDef(  new ClassName("foo"),
                                
//                                 new Constructor(vardec, new BreakStatement()),
//                                 instanceVars,
//                                 methodDefs));
//     //Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, null);
//     Typechecker.typecheckProgram(program);


// }

// @Test(expected = TypeErrorException.class) 
// public void testUniqueNamesForVariables() {

// /*
//     class Foo{
//     private int x;  
//     private int x;  --> error cuz same variable names
//     Foo() 
//         x =4; 
//     }
// */

//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<MethodDef> methodDefs = new ArrayList<>();

//     VarDec[] varDec = {}; 
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("x"))));
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("x"))));

//     classDef.add(new ClassDef(  new ClassName("foo"),
                                
//                                 new Constructor(vardec, new VarAssignment(new Variable("x"), new NumberExp(4))),
//                                 instanceVars,
//                                 methodDefs));
//     //Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, null);
//     Typechecker.typecheckProgram(program);


// }


// @Test(expected = TypeErrorException.class) 
// public void testCLassesWithSameNames() {

// /*
//     class Foo{
//     int x;
//     Foo() 
//         x =4; 
//     }
//     class Foo{ ---> class names can't be same
    
//         public void method()
//             int z = 0;
//     }
// */

//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<InstanceDec> instanceVars2 = new ArrayList<>();
//     List<MethodDef> methodDefs = new ArrayList<>();
//     List<MethodDef> methodDefs2 = new ArrayList<>();


//     methodDefs.add(new MethodDef(new PublicAccess(), new VoidType(), new MethodName("method"), varDec, new VarDecAssignment(new VarDec(new IntType(), new Variable("z")), new NumberExp(0))));
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("x"))));

//     VarDec[] varDec = {};
    
//     classDef.add(new ClassDef(new ClassName("foo"),
                                
//                                 new Constructor(vardec, new VarAssignment(new Variable("x"), new NumberExp(4))),
//                                 instanceVars,
//                                 methodDefs));

//     classDef.add(new ClassDef(new ClassName("foo"),
                                
//                                 null,
//                                 instanceVars2,
//                                 methodDefs2));
//     //Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, null);
//     Typechecker.typecheckProgram(program);


// }

// /**stuck here **/
// /*@Test(expected = TypeErrorException.class) 
// public void testFunnctionCallsWithRightParameters() {
// /*
//     class Foo{
//     private int y;
//     Foo() 
//         set(4); 
//     }
//     public void setY(int x) ---> should have a return statement
//         y =4; 
//     List<ClassDef> classDef = new ArrayList<>();
//     List<InstanceDec> instanceVars = new ArrayList<>();
//     List<MethodDef> methodDefs = new ArrayList<>();
//     VarDec[] varDec = {}; 
    
//     instanceVars.add(new InstanceDec(new PrivateAccess(), new VarDec(new IntType(), new Variable("y"))));
//     methodDefs.add(new MethodDef(new PublicAccess(), new VoidType(), new MethodName("setY"), varDec, new VarAssignment(new Variable("y"), new NumberExp(4))));
//     classDef.add(new ClassDef(  new ClassName("foo"),
                                
//                                 new Constructor(vardec, new VarAssignment(new Variable("x"), new NumberExp(4))),
//                                 instanceVars,
//                                 methodDefs));
//     //Statement statment = new VarDecAssignment(new VarDec(new IntType(), new Variable("x")), new NumberExp(0));
//     final Program program = new Program(classDef, null);
//     Typechecker.typecheckProgram(program);
// }*/

// //whats ur business logic




// /*@Test(expected = TypeErrorException.class) 
// public void blah() {
//     final Program program;
//     Typechecker.typecheckProgram(program);
// }
//     /*public static final StructureDeclaration[] EMPTY_STRUCTURES =
//         new StructureDeclaration[0];
//     public static final FunctionDefinition[] EMPTY_FUNCTIONS =
//         new FunctionDefinition[0];
//     public static final VariableDeclaration[] EMPTY_VARDECS =
//         new VariableDeclaration[0];
    
//     public static Stmt stmts(final Stmt... input) {
//         assert(input.length > 0);
//         Stmt result = input[input.length - 1];
//         for (int index = input.length - 2; index >= 0; index--) {
//             result = new SequenceStmt(input[index], result);
//         }
//         return result;
//     }
//     public static VariableDeclarationInitializationStmt def(final Type type, final String name, final Exp exp) {
//         return new VariableDeclarationInitializationStmt(new VariableDeclaration(type, new Variable(name)), exp);
//     }
//     // void foo() {
//     //   body
//     // }
//     public static FunctionDefinition voidFunction(final Stmt body) {
//         return new FunctionDefinition(new VoidType(),
//                                       new FunctionName("foo"),
//                                       EMPTY_VARDECS,
//                                       body);
//     }*/
// }