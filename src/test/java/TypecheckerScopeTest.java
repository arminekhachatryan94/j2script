package j2script.typechecker;

import j2script.*;

import org.junit.Test;

public class TypecheckerScopeTest {
    public static final StructureDeclaration[] EMPTY_STRUCTURES =
        new StructureDeclaration[0];
    public static final FunctionDefinition[] EMPTY_FUNCTIONS =
        new FunctionDefinition[0];
    public static final VariableDeclaration[] EMPTY_VARDECS =
        new VariableDeclaration[0];
    
    public static Stmt stmts(final Stmt... input) {
        assert(input.length > 0);
        Stmt result = input[input.length - 1];
        for (int index = input.length - 2; index >= 0; index--) {
            result = new SequenceStmt(input[index], result);
        }
        return result;
    }

    public static VariableDeclarationInitializationStmt def(final Type type, final String name, final Exp exp) {
        return new VariableDeclarationInitializationStmt(new VariableDeclaration(type, new Variable(name)), exp);
    }

    // void foo() {
    //   body
    // }
    public static FunctionDefinition voidFunction(final Stmt body) {
        return new FunctionDefinition(new VoidType(),
                                      new FunctionName("foo"),
                                      EMPTY_VARDECS,
                                      body);
    }
}
