package j2script;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.AbstractMap;
import java.util.List;
import java.util.ArrayList;

import j2script.util.*;
import j2script.TypeErrorException;
import j2script.declarations.*;
import j2script.expressions.*;
import j2script.names.*;
import j2script.operators.*;
import j2script.statements.*;
import j2script.types.*;


public class TypeChecker {
    // maps name of class to its instance variables
    private final Map<ClassName, LinkedHashMap<Variable, Type>> instanceVars;
    // maps name of class to its method definitions
    private final Map<ClassName, LinkedHashMap<MethodName, Pair<Type[], ReturnType>>> methodDefs;

    private TypeChecker(final Program program) throws TypeErrorException {
        // have to load these before checking structure or function validity
        instanceVars = makeInstanceVarMapping(program.classDefs);
        ensureInstanceVarsValid();

        methodDefs = makeMethodDefMapping(program.classDefs);
        for(final ClassDef classDef : program.classDefs) {
            for(final MethodDef methodDef : classDef.methodDefs) {
                typecheckMethodDef(methodDef);
            }
        }
        final InScope statementScope = new InScope(new VoidType(),
                                                  new HashMap<Variable, Type>(),
                                                  false);
        statementScope.typecheckStatement(program.statement);
    }

    // not permitted to have repeated field names in the same class
    // fields cannot have void types
    private static LinkedHashMap<Variable, Type>
        makeFieldMapping(final List<VarDec> fields) throws TypeErrorException {
        
        final LinkedHashMap<Variable, Type> result =
            new LinkedHashMap<Variable, Type>();

        for (final VarDec dec : fields) {
            ensureNonVoidType(dec.type);
            result.put(new Variable(dec.var.name), dec.type);
        }

        if (result.size() != fields.size()) {
            throw new TypeErrorException("Duplicate field name");
        }

        return result;
    }

    // not permitted to have multiple class declarations with the same name
    private Map<ClassName, LinkedHashMap<Variable, Type>>
        makeInstanceVarMapping(final List<ClassDef> clasDefs) throws TypeErrorException {
        final Map<ClassName, LinkedHashMap<Variable, Type>> result =
            new HashMap<ClassName, LinkedHashMap<Variable, Type>>();

        for (final ClassDef def : clasDefs) {
            final LinkedHashMap<Variable, Type> instanceVarMapping =
                makeFieldMapping(def.instanceVars);
            result.put(def.name, instanceVarMapping);
        }

        if (result.size() != clasDefs.size()) {
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

    // not permitted to have multiple methods with the samfirste name in a class
    private LinkedHashMap<MethodName, Pair<Type[], ReturnType>>
        makeMethodMapping(final List<MethodDef> methodDefs) throws TypeErrorException {

        final LinkedHashMap<MethodName, Pair<Type[], ReturnType>> result =
            new LinkedHashMap<MethodName, Pair<Type[], ReturnType>>();

        for (final MethodDef def : methodDefs) {
            final Type[] parameters = parameterTypes(def.varDecs);
            final Pair<Type[], ReturnType> value =
                new Pair<Type[], ReturnType>(parameters, def.returnType);
            result.put(def.name, value);
        }

        if (result.size() != methodDefs.size()) {
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

    private void typecheckMethodDef(MethodDef mdef) throws TypeErrorException {
        final InScope initialScope = new InScope(mdef.returnType,
                                                 initialVariableMapping(mdef.varDecs),
                                                 false);
        final Pair<InScope, Boolean> stmtResult = initialScope.typecheckStatement(mdef.statement);

        if (!stmtResult.second.booleanValue() &&
            !(mdef.returnType instanceof VoidType)) {
            throw new TypeErrorException("Missing return in " + mdef.name.toString());
        }
    }

    // error if duplicate variable names are used
    private static Map<Variable, Type> initialVariableMapping(final VarDec[] parameters) throws TypeErrorException {
        final Map<Variable, Type> result = new HashMap<Variable, Type>();

        for (final VarDec dec : parameters) {
            result.put(dec.var, dec.type);
        }

        if (result.size() != parameters.length) {
            throw new TypeErrorException("Duplicate variable name in function parameters");
        }

        return result;
    }

    // Checks if a classtype has been defined
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
                return new BooleanType();
            } else if (exp instanceof VariableExp) {
                return lookupVariable(((VariableExp)exp).var);
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
                ClassExp classExp = (ClassExp) exp;
                return new ClassType(classExp.name);
            } else if(exp instanceof StringExp) {
              StringExp stringExp = (StringExp) exp;
              return new StringType(stringExp.name);
            } else {
              assert(false);
              throw new TypeErrorException("Unrecognized expression: " + exp.toString());
            }
        } // typeofExp

        // returns any new scope to use, along with whether or not return was observed on
        // all paths
        public Pair<InScope, Boolean> typecheckStatement(final Statement stmt) throws TypeErrorException {
            if (stmt instanceof ExpStatement) {
                final ExpStatement expStmt = (ExpStatement)stmt;
                typeofExp(expStmt.exp);
                return new Pair<InScope, Boolean>(this, Boolean.valueOf(false));
            } else if (stmt instanceof IfStatement) {
                final IfStatement asIf = (IfStatement)stmt;
                ensureTypesSame(new BooleanType(), typeofExp(asIf.guard));

                // since the true and false branches form their own blocks, we
                // don't care about any variables they put in scope
                final Pair<InScope, Boolean> leftResult = typecheckStatement(asIf.ifTrue);
                final Pair<InScope, Boolean> rightResult = typecheckStatement(asIf.ifFalse);
                final boolean returnOnBoth =
                    leftResult.second.booleanValue() && rightResult.second.booleanValue();
                return new Pair<InScope, Boolean>(this, returnOnBoth);
            } else if (stmt instanceof WhileStatement) {
                final WhileStatement asWhile = (WhileStatement)stmt;
                ensureTypesSame(new BooleanType(), typeofExp(asWhile.condition));

                // Don't care about variables in the while.
                // Because the body of the while loop will never execute if the condition is
                // initially false, even if all paths in the while loop have return, this doesn't
                // mean that we are guaranteed to hit return.
                setInWhile().typecheckStatement(asWhile.stmt);
                return new Pair<InScope, Boolean>(this, Boolean.valueOf(false));
            } else if (stmt instanceof BreakStatement) {
                if (!inWhile) {
                    throw new TypeErrorException("Break outside of loop");
                }
                return new Pair<InScope, Boolean>(this, Boolean.valueOf(false));
            } else if (stmt instanceof VarDecAssignment) {
                final VarDecAssignment dec =
                    (VarDecAssignment)stmt;
                final Type expectedType = dec.varDec.type;
                ensureNonVoidType(expectedType);
                ensureValidType(expectedType);
                ensureTypesSame(expectedType,
                                typeofExp(dec.exp));
                final InScope resultInScope =
                    addVariable(dec.varDec.var, expectedType);
                return new Pair<InScope, Boolean>(resultInScope, Boolean.valueOf(false));
            } else if (stmt instanceof VarAssignment) {
                final VarAssignment assign = (VarAssignment)stmt;
                // check if exists
                Type varType = lookupVariable(assign.variable);
                // chack if right type
                ensureTypesSame(varType, typeofExp(assign.exp));
                return new Pair<InScope, Boolean>(this, Boolean.valueOf(false));
            } else if (stmt instanceof ReturnVoidStatement) {
                ensureTypesSame(new VoidType(), returnType);
                return new Pair<InScope, Boolean>(this, Boolean.valueOf(true));
            } else if (stmt instanceof ReturnExpStatement) {
                final Type receivedType = typeofExp(((ReturnExpStatement)stmt).exp);
                ensureTypesSame(receivedType, returnType);
                return new Pair<InScope, Boolean>(this, Boolean.valueOf(true));
            } else if (stmt instanceof PrintStatement) {
                final PrintStatement expStmt = (PrintStatement)stmt;
                ensureTypesSame(new StringType(new StringName(null)), typeofExp(expStmt.exp));
                return new Pair<InScope, Boolean>(this, Boolean.valueOf(false));
            } else if (stmt instanceof Block) {
                final Block block = (Block)stmt;
                for (Statement statement : block.statements) {
                  typecheckStatement(statement);
                }
                return new Pair<InScope, Boolean>(this, Boolean.valueOf(false));
            } else {
                assert(false);
                throw new TypeErrorException("Unrecognized statement: " + stmt.toString());
            }
        } // typecheckStatement
    } // InScope

    private static Type binopType(final Type left, final Op op, final Type right) throws TypeErrorException {
        final IntType intType = new IntType();
        if (op instanceof PlusOp) {
            // TWO kinds are permitted:
            // int + int: returns int
            // pointer + int: returns same pointer type
            //
            // in both cases, the right side is an int
            ensureTypesSame(intType, right);
            if (left instanceof IntType) {
                // int + int returns int
                return intType;
            } else {
                throw new TypeErrorException("invalid lhs for +: " + left.toString());
            }
        } else if (op instanceof MinusOp ||
                   op instanceof MultOp ||
                   op instanceof DivOp) {
            // int (-|*|/) int = int
            ensureTypesSame(intType, left);
            ensureTypesSame(intType, right);
            return intType;
        // } else if (op instanceof EqualsOp) {
        //     // type == type = boolean
        //     // both need to be of the same type
        //     ensureTypesSame(left, right);
        //     return new BoolType();                     USE LATER IF ADD == AND/OR <
        // } else if (op instanceof LessThanOp) {
        //     // int < int = boolean
        //     ensureTypesSame(intType, left);
        //     ensureTypesSame(intType, right);
        //     return new BoolType();
        } else {
            // should be no other operators
            assert(false);
            throw new TypeErrorException("Unknown operator: " + op.toString());
        }
    } // binopType

    // intended for testing
    private Type expTypeNoScopeForTesting(final Exp exp) throws TypeErrorException {
        return new InScope(new VoidType(),
                           new HashMap<Variable, Type>(),
                           false).typeofExp(exp);
    }

    // intended for testing
    public static Type expTypeForTesting(final Exp exp) throws TypeErrorException {
        final TypeChecker checker =
            new TypeChecker(new Program(new ArrayList<ClassDef>(),
                                        new Block(new ArrayList())));
        return checker.expTypeNoScopeForTesting(exp);
    }

    // Called in testing to type check the given program
    public static void typecheckProgram(final Program program) throws TypeErrorException {
        new TypeChecker(program);
    }
}