package j2script;

public class TypeChecker {
    // maps name of class to its instance variables and methods in a pair
    private final Map<ClassName, Pair<LinkedHashMap<Variable, Type>, 
                                      LinkedHashMap<MethodName, 
                                      Pair<Type[], ReturnType>>>> classMapping;
    // /me cries in java

    private Typechecker(final Program program) throws TypeErrorException {
        // have to load these before checking class or function validity
        // classDecs = makeClassMapping(program.classDecs);
        // ensureStructureFieldsValid();

        functionDefs = makeFunctionMapping(program.functionDefs);

        for (final FunctionDefinition def : program.functionDefs) {
            typecheckFunctionDef(def);
        }
    }

    private class InScope {
        // return type of the function we are currently in
        private final Type returnType;
        // maps variables in scope to their corresponding type
        private final Map<Variable, Type> inScope;
        // records if we are in a while loop or not
        private final boolean inWhile;

    // Called in testing to type check the given program
    public static void typecheckProgram(final Program program) throws TypeErrorException {
        new Typechecker(program);
    }

    private static void ensureNonVoidType(final Type type) throws TypeErrorException {
        if (type instanceof VoidType) {
            throw new TypeErrorException("Void type illegal here");
        }
    }

    // not permitted to have repeated instance vars names in the same class
    // instance vars cannot have void types
    private static LinkedHashMap<Variable, Type>
        makeInstanceMapping(final VariableDeclaration[] fields) throws TypeErrorException {
        
        final LinkedHashMap<Variable, Type> result =
            new LinkedHashMap<Variable, Type>();

        for (final VariableDeclaration dec : fields) {
            ensureNonVoidType(dec.type);
            //result.put(new Variable(dec.variable.name), dec.type);
        }

        if (result.size() != fields.length) {
            throw new TypeErrorException("Duplicate field name");
        }

        return result;
    }

    private void typecheckMethodDef(final MethodDefinition mdef) throws TypeErrorException {
        final InScope initialScope = new InScope(mdef.returnType,
                                                 initialVariableMapping(mdef.parameters),
                                                 false);
        final Pair<InScope, Boolean> stmtResult = initialScope.typecheckStmt(mdef.statement);

        if (!stmtResult.second.booleanValue() &&
            !(mdef.returnType instanceof VoidType)) {
            throw new TypeErrorException("Missing return in " + mdef.name.toString());
        }
    }

    // error if duplicate variable names are used
    private static Map<Variable, Type> initialVariableMapping(final VariableDeclaration[] parameters) throws TypeErrorException {
        final Map<Variable, Type> result = new HashMap<Variable, Type>();

        for (final VariableDeclaration dec : parameters) {
            result.put(dec.variable, dec.type);
        }

        if (result.size() != parameters.length) {
            throw new TypeErrorException("Duplicate variable name in function parameters");
        }

        return result;
    }

    private static void ensureTypesSame(final Type expected, final Type received) throws TypeErrorException {
        if (!expected.equals(received)) {
            throw new TypeErrorException(expected, received);
        }
    }
}