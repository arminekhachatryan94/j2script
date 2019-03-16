package j2script;

import java.util.LinkedHashMap;

import j2script.TypeErrorException;
import j2script.declarations.MethodDef;
import j2script.declarations.Program;
import j2script.expressions.ClassExp;
import j2script.expressions.NumberExp;
import j2script.types.Type;
import j2script.types.VoidType;
import parser.types.ClassType;

public class TypeChecker {
    // maps name of class to its instance variables
    private final Map<ClassName, LinkedHashMap<Variable, Type>> instanceVars;
    // maps name of class to its method definitions
    private final Map<ClassName, LinkedHashMap<MethodName, Pair<Type[], ReturnType>>> methodDefs;

    private Typechecker(final Program program) throws TypeErrorException {
        // have to load these before checking structure or function validity
        instanceVars = makeInstanceVarMapping(program.classDefs);
        ensureInstanceVarsValid();

        methodDefs = makeMethodDefMapping(program.classDefs);
        typecheckMethods();  // Not done
    }

    // not permitted to have repeated field names in the same class
    // fields cannot have void types
    private static LinkedHashMap<Variable, Type>
        makeFieldMapping(final List<VarDec> fields) throws TypeErrorException {
        
        final LinkedHashMap<Variable, Type> result =
            new LinkedHashMap<Variable, Type>();

        for (final VarDec dec : fields) {
            ensureNonVoidType(dec.type);
            result.put(new Variable(dec.variable.name), dec.type);
        }

        if (result.size() != fields.length) {
            throw new TypeErrorException("Duplicate field name");
        }

        return result;
    }

    // not permitted to have multiple class declarations with the same name
    private static Map<ClassName, LinkedHashMap<Variable, Type>>
        makeInstanceVarMapping(final List<ClassDef> clasDefs) throws TypeErrorException {
        final Map<ClassName, LinkedHashMap<Variable, Type>> result =
            new HashMap<ClassName, LinkedHashMap<Variable, Type>>();

        for (final ClassDef def : clasDefs) {
            final LinkedHashMap<Variable, Type> instanceVarMapping =
                makeFieldMapping(def.fields);
            result.put(def.name, fieldMapping);
        }

        if (result.size() != clasDefs.length) {
            throw new TypeErrorException("Duplicate structure name");
        }

        return result;
    }

    // makes sure that class fields don't refer to non-existent classes
    private void ensureInstanceVarsValid() throws TypeErrorException {
        for (final LinkedHashMap<Variable, Type> fields : instanceVars.values()) {
            for (final Type type : fields.values()) {
                ensureValidType(type);
            }
        }
    }

    // throws exception if any are void
    private Type[] parameterTypes(final VarDec[] vars) throws TypeErrorException {
        final Type[] result = new Type[vars.length];

        for (int index = 0; index < vars.length; index++) {
            final Type current = vars[index].type;
            ensureValidType(current);
            ensureNonVoidType(current);
            result[index] = current;
        }

        return result;
    }

    // not permitted to have multiple methods with the same name in a class
    private LinkedHashMap<MethodName, Pair<Type[], ReturnType>>
        makeMethodMapping(final List<MethodDef> methodDefs) throws TypeErrorException {

        final LinkedHashMap<MethodName, Pair<Type[], ReturnType>> result =
            new LinkedHashMap<MethodName, Pair<Type[], ReturnType>>();

        for (final MethodDef def : method) {
            final Type[] parameters = parameterTypes(def.varDecs);
            final Pair<Type[], Type> value =
                new Pair<Type[], Type>(parameters, def.returnType);
            result.put(def.name, value);
        }

        if (result.size() != functions.length) {
            throw new TypeErrorException("Duplicate method name");
        }

        return result;
    }

    private Map<ClassName, LinkedHashMap<MethodName, Pair<Type[], ReturnType>>> 
        makeMethodDefMapping(final List<ClassDef> clasDefs) throws TypeErrorException {

        final Map<ClassName, LinkedHashMap<MethodName, Pair<Type[], ReturnType>>> result =
            new HashMap<ClassName, LinkedHashMap<MethodName, Pair<Type[], ReturnType>>>();

        for (final ClassDef def : clasDefs) {
            final LinkedHashMap<MethodName, Pair<Type[], ReturnType>> methodMapping =
                makeMethodMapping(def.methodDefs);
            result.put(def.name, methodMapping);
        }

        return result;
    }

    private void typecheckMethods() throws TypeErrorException {
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

    private void ensureValidType(final Type type) throws TypeErrorException {
        if (type instanceof ClassType) {
            final ClassName name = ((ClassType)type).name;
            if (!instanceVars.containsKey(name)) {
                throw new TypeErrorException("Non-existent class referenced: " +
                                             name.toString());
            }
        }
    }

    private static void ensureTypesSame(final Type expected, final Type received) throws TypeErrorException {
        if (!expected.equals(received)) {
            throw new TypeErrorException(expected, received);
        }
    }

    private static void ensureNonVoidType(final Type type) throws TypeErrorException {
        if (type instanceof VoidType) {
            throw new TypeErrorException("Void type illegal here");
        }
    }

    private class InScope {
        // return type of the function we are currently in
        private final Type returnType;
        // maps variables in scope to their corresponding type
        private final Map<Variable, Type> inScope;
        // records if we are in a while loop or not
        private final boolean inWhile;

        public InScope(final Type returnType,
                        final Map<Variable, Type> inScope,
                        final boolean inWhile) {
            this.returnType = returnType;
            this.inScope = inScope;
            this.inWhile = inWhile;
        }

        private InScope addVariable(final Variable variable,
                                    final Type variableType) {
            final Map<Variable, Type> copy =
                new HashMap<Variable, Type>(inScope);
            copy.put(variable, variableType);
            return new InScope(returnType, copy, inWhile);
        }

        private InScope setInWhile() {
            return new InScope(returnType, inScope, true);
        }
 
        private Type typeofAccess(final Type maybeStructureType,
                final FieldName field) throws TypeErrorException {
            if (maybeStructureType instanceof StructureType) {
                final StructureName name = ((StructureType)maybeStructureType).name;
                final LinkedHashMap<FieldName, Type> expected = structDecs.get(name);
                if (expected != null) {
                    final Type fieldType = expected.get(field);
                    if (fieldType != null) {
                        return fieldType;
                    } else {
                        throw new TypeErrorException("Structure " + name.toString() +
                            " does not have field " + field.toString());
                    }
                } else {
                    throw new TypeErrorException("No structure with name: " + name.toString());
                }
            } else {
                throw new TypeErrorException("Expected structure type; received: " +
                                            maybeStructureType.toString());
            }
        }
             
        private Type[] typeofExps(final Exp[] exps) throws TypeErrorException {
            final Type[] types = new Type[exps.length];
            for (int index = 0; index < exps.length; index++) {
                types[index] = typeofExp(exps[index]);
            }
            return types;
        }

        // Look up the type of the variable.
        // If it's not present in the map, then it's not in scope.
        private Type lookupVariable(final Variable var) throws TypeErrorException {
            final Type varType = inScope.get(var);
            if (varType == null) {
                throw new TypeErrorException("Variable not in scope: " + var);
            }
            return varType;
        }
 
        public Type typeofExp(final Exp exp) throws TypeErrorException {
            if (exp instanceof NumberExp) {
                return new IntType();
            } else if (exp instanceof BoolExp) {
                return new BoolType();
            } else if (exp instanceof VariableExp) {
                return lookupVariable(((VariableExp)exp).variable);
            } else if (exp instanceof BinopExp) {
                // the return type and expected parameter types all depend
                // on the operator.  In all cases, we need to get the types
                // of the operands, and then check if this matches with the
                // operator
                final BinopExp asBinop = (BinopExp)exp;
                final Type leftType = typeofExp(asBinop.left);
                final Type rightType = typeofExp(asBinop.right);
                return binopType(leftType, asBinop.op, rightType);
            } else if(exp instanceof ClassExp) {
                return new ClassType(exp.name);
            } else if(exp instanceof VarMethodExp){
                return new 
            }
            
            
            else if (exp instanceof MakeStructureExp) {
                final MakeStructureExp asStruct = (MakeStructureExp)exp;
                checkMakeStructure(asStruct.name,
                                    typeofExps(asStruct.parameters));
                return new StructureType(asStruct.name);
            } else if (exp instanceof FunctionCallExp) {
                final FunctionCallExp asCall = (FunctionCallExp)exp;
                return checkFunctionCall(asCall.name,
                                        typeofExps(asCall.parameters));
            } else if (exp instanceof CastExp) {
                // Explicit cast.  Trust the user.  Ideally, we'd check
                // this at runtime.  We still need to look at the expression
                // to make sure that this is itself well-typed.
                final CastExp asCast = (CastExp)exp;
                typeofExp(asCast.exp);
                return asCast.type;
            } else if (exp instanceof FieldAccessExp) {
                final FieldAccessExp asAccess = (FieldAccessExp)exp;
                return typeofAccess(typeofExp(asAccess.exp),
                                    asAccess.field);
            } else {
                assert(false);
                throw new TypeErrorException("Unrecognized expression: " + exp.toString());
            }
        } // typeofExp

        // returns any new scope to use, along with whether or not return was observed on
        // all paths
        public Pair<InScope, Boolean> typecheckStmt(final Stmt stmt) throws TypeErrorException {
            if (stmt instanceof IfStmt) {
                final IfStmt asIf = (IfStmt)stmt;
                ensureTypesSame(new BoolType(), typeofExp(asIf.guard));

                // since the true and false branches form their own blocks, we
                // don't care about any variables they put in scope
                final Pair<InScope, Boolean> leftResult = typecheckStmt(asIf.ifTrue);
                final Pair<InScope, Boolean> rightResult = typecheckStmt(asIf.ifFalse);
                final boolean returnOnBoth =
                    leftResult.second.booleanValue() && rightResult.second.booleanValue();
                return new Pair<InScope, Boolean>(this, returnOnBoth);
            } else if (stmt instanceof WhileStmt) {
                final WhileStmt asWhile = (WhileStmt)stmt;
                ensureTypesSame(new BoolType(), typeofExp(asWhile.guard));

                // Don't care about variables in the while.
                // Because the body of the while loop will never execute if the condition is
                // initially false, even if all paths in the while loop have return, this doesn't
                // mean that we are guaranteed to hit return.
                setInWhile().typecheckStmt(asWhile.body);
                return new Pair<InScope, Boolean>(this, Boolean.valueOf(false));
            } else if (stmt instanceof BreakStmt ||
                        stmt instanceof ContinueStmt) {
                if (!inWhile) {
                    throw new TypeErrorException("Break or continue outside of loop");
                }
                return new Pair<InScope, Boolean>(this, Boolean.valueOf(false));
            } else if (stmt instanceof VariableDeclarationInitializationStmt) {
                final VariableDeclarationInitializationStmt dec =
                    (VariableDeclarationInitializationStmt)stmt;
                final Type expectedType = dec.varDec.type;
                ensureNonVoidType(expectedType);
                ensureValidType(expectedType);
                ensureTypesSame(expectedType,
                                typeofExp(dec.exp));
                final InScope resultInScope =
                    addVariable(dec.varDec.variable, expectedType);
                return new Pair<InScope, Boolean>(resultInScope, Boolean.valueOf(false));
            } else if (stmt instanceof ReturnVoidStmt) {
                ensureTypesSame(new VoidType(), returnType);
                return new Pair<InScope, Boolean>(this, Boolean.valueOf(true));
            } else if (stmt instanceof ReturnExpStmt) {
                final Type receivedType = typeofExp(((ReturnExpStmt)stmt).exp);
                ensureTypesSame(returnType, receivedType);
                return new Pair<InScope, Boolean>(this, Boolean.valueOf(true));
            } else if (stmt instanceof FreeStmt) {
                ensureTypesSame(new PointerType(new VoidType()),
                                typeofExp(((FreeStmt)stmt).value));
                return new Pair<InScope, Boolean>(this, Boolean.valueOf(false));
            } else if (stmt instanceof SequenceStmt) {
                final SequenceStmt asSeq = (SequenceStmt)stmt;
                final Pair<InScope, Boolean> fromLeft = typecheckStmt(asSeq.first);
                
                if (fromLeft.second.booleanValue()) {
                    throw new TypeErrorException("Dead code from early return");
                }

                return fromLeft.first.typecheckStmt(asSeq.second);
            } else if (stmt instanceof ExpStmt) {
                // Just need to check that it's well-typed.  Permitted to
                // return anything.
                typeofExp(((ExpStmt)stmt).exp);
                return new Pair<InScope, Boolean>(this, Boolean.valueOf(false));
            } else {
                assert(false);
                throw new TypeErrorException("Unrecognized statement: " + stmt.toString());
            }
        } // typecheckStmt
    } // InScope

    // intended for testing
    private Type expTypeNoScopeForTesting(final Exp exp) throws TypeErrorException {
        return new InScope(new VoidType(),
                           new HashMap<Variable, Type>(),
                           false).typeofExp(exp);
    }

    // intended for testing
    public static Type expTypeForTesting(final Exp exp) throws TypeErrorException {
        final Typechecker checker =
            new Typechecker(new Program(new List<ClassDef>(),
                                        new Statement));
        return checker.expTypeNoScopeForTesting(exp);
    }

    // Called in testing to type check the given program
    public static void typecheckProgram(final Program program) throws TypeErrorException {
        new Typechecker(program);
    }
}