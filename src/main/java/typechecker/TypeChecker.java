package j2script;

import j2script.TypeEnvironment;
import j2script.TypeErrorException;
import j2script.access.*;
import j2script.declarations.*;
import j2script.expressions.*;
import j2script.names.*;
import j2script.operators.*;
import j2script.statements.*;
import j2script.types.*;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class TypeChecker {
    private static Map<ClassName, ClassDef> classes;

    private TypeChecker(final Map<ClassName, ClassDef> classes) throws TypeErrorException {
        this.classes = classes;
    }

    public static ClassDef getClass(final ClassName name) throws TypeErrorException {
        final ClassDef result = classes.get(name);
        if (result == null) {
            throw new TypeErrorException("No such class defined: " + name);
        } else {
            return result;
        }
    } // getClass

    public static Set<TypeVariable> asSet(final List<TypeVariable> typeVariables) throws TypeErrorException {
        final Set<TypeVariable> result = new HashSet<TypeVariable>();
        for (final TypeVariable typeVariable : typeVariables) {
            if (result.contains(typeVariable)) {
                throw new TypeErrorException("Duplicate type variable introduced: " + typeVariable);
            }
            result.add(typeVariable);
        }
        return result;
    } // asSet

    public static boolean isPrimitive(Type type) {
        return type.equals(new IntType()) || 
               type.equals(new BooleanType());
    }

    private Constructor getConstructor(final ClassName onClass) throws TypeErrorException{
        final ClassDef classDef = getClass(onClass);
        return classDef.constructor;
    }

    private static ClassType asSupertype(final ClassType type) throws TypeErrorException {
        final ClassDef abstracted = getClass(type.name);
        if (abstracted.extendedClass == null) {
            throw new TypeErrorException("Has no supertype: " + type);
        }
        final ClassDef specialized = TypeRewriter.rewriteClassDef(abstracted, type.types);
        return new ClassType(specialized.extendedClass.extendsName,
                             specialized.extendedClass.types);
    } // asSupertype

    // Checks if class types are comparable
    public static void typesOK(final Type baseType, final Type subType) throws TypeErrorException {
        if (!baseType.equals(subType)) {
            // see if subType is a subtype of base type
            if (baseType instanceof ClassType && subType instanceof ClassType) {
                typesOK(baseType, asSupertype((ClassType)subType));
            } else {
                throw new TypeErrorException("Base type " + baseType + " is not comparable to " + subType);
            }
        }
    } // typesOk

    // returns null if it couldn't find it
    public MethodDef findMethodDirect(final ClassDef onClass,
                                    final MethodName methodName) throws TypeErrorException {
        for (final MethodDef methodDef : onClass.methodDefs) {
            if (methodDef.name.equals(methodName)) {
                return methodDef;
            }
        }
        return null;
    } // findMethodDirect

    public MethodDef findMethod(final ClassType onClass,
                                final MethodName methodName) throws TypeErrorException {
        final ClassDef abstractedClass = getClass(onClass.name);
        final ClassDef specializedClass = TypeRewriter.rewriteClassDef(abstractedClass, onClass.types);
        final MethodDef result = findMethodDirect(specializedClass, methodName);

        if (result == null) {
            return findMethod(asSupertype(onClass), methodName);
        } else {
            return result;
        }
    } // findMethod

    private void checkParameters(final TypeEnvironment env, List<VarDec> formalParams, List<Exp> actualParams) throws TypeErrorException {
        if (formalParams.size() != actualParams.size()) {
            throw new TypeErrorException("Arity mismatch in parameters; expected: " +
                                            formalParams.size() +
                                            "; received: " +
                                            actualParams.size());
        } else {
            for (int i = 0; i < formalParams.size(); i++) {
                env.typeInScope(formalParams.get(i).type);
                final Type expected = formalParams.get(i).type;
                final Type actual = typeofExp(env, actualParams.get(i));
                typesOK(expected, actual);
            }
        }
    }

    // Check for redefined instance vars from parent later
    public static void noDuplicates(final List<VarDec> params) throws TypeErrorException {
        final Set<Variable> seen = new HashSet<>();
        for (final VarDec current : params) {
            if (seen.contains(current.var)) {
                throw new TypeErrorException("Duplicate variable: " + current.var);
            }
            seen.add(current.var);
        }
    } // noDuplicates

    // Checks to see if there is a duplicate method defined
    // Not checking names since overloaded methods are allowed
    public static void noDuplicateMethodDefs(final List<MethodDef> methods) throws TypeErrorException {
    final List<MethodDef> seen = new ArrayList<MethodDef>();
        for (int i = 0; i < methods.size(); i++) {
            for(int j = i+1; j < methods.size(); j++) {
                if(methods.get(i) != methods.get(j)) {
                // check if same
                if(methods.get(i).name == methods.get(i).name && 
                    Arrays.deepEquals(methods.get(i).varDecs.toArray(), methods.get(j).varDecs.toArray())) {
                        throw new TypeErrorException("Duplicate method defnied: " + methods.get(i).name);
                    }
                }
            }
        }
    } // noDuplicateMethodDefs

  // checks that subclasses don't redefined parent class instance variables
    public void instanceVariablesOk(final Set<Variable> seen, final ClassName current) throws TypeErrorException {
        final ClassDef classDef = getClass(current);
        for (final VarDec param : classDef.instanceVars) {
            if (seen.contains(param.var)) {
                throw new TypeErrorException("Instance variable " + param.var + " redefined in " + ": " + current.name);
            }
            seen.add(param.var);
        }
        if (classDef.extendedClass != null) {
            instanceVariablesOk(seen, classDef.extendedClass.extendsName);
        }
    } // instanceVariablesOk

    // checks that subclasses don't redefined parent class instance variables
    public void instanceVariablesOk(final ClassName current) throws TypeErrorException {
        instanceVariablesOk(new HashSet<Variable>(), current);
    } // instanceVariablesOk

    public static boolean containsNoReturns(final List<Statement> statements) {
        for (int i = 0; i < statements.size(); i++) {
            if (statements.get(i) instanceof ReturnVoidStatement ||
                statements.get(i) instanceof ReturnExpStatement) {
                return false;
            }
        }
        return true;
    } // containsNoReturns

    public static boolean containsNoSupers(final List<Statement> statements) {
        for (final Statement stmt : statements) {
            if (stmt instanceof SuperStatement) {
                return false;
            }
        }
        return true;
    } // containsNoSupers

    public static void superReturnOkInMethod(final Statement stmt) throws TypeErrorException {
        List<Statement> statements = new ArrayList<>();
        if (stmt instanceof Block) {
            statements = ((Block)stmt).statements;
        } else {
            statements.add(stmt);
        }

        if (statements.size() == 0 || containsNoReturns(statements)) {
            throw new TypeErrorException("Missing return");
        }
        if (!containsNoSupers(statements)) {
            throw new TypeErrorException("methods cannot contain super");
        }
    } // superReturnOkInMethod

    //  Checks if super call is OK
    public static void superReturnOkInConstructor(final boolean isBaseClass,
                                                final Statement stmt) throws TypeErrorException {
        List<Statement> statements = new ArrayList<>();
        if (stmt instanceof Block) {
            statements = ((Block) stmt).statements;
        } else {
            statements.add(stmt);
        }

        // is baseclass and contains super
        if (isBaseClass && !containsNoSupers(statements)) {
            throw new TypeErrorException("base classes cannot contain super");
        }
        // is not baseclass and has no statements or the first statement is not super
        if (!isBaseClass &&
            (statements.size() == 0 || !(statements.get(0) instanceof SuperStatement))) {
            throw new TypeErrorException("super needs to be first in subclass constructor");
        }
        // constructor cannot have return
        if (!containsNoReturns(statements)) {
            throw new TypeErrorException("return in constructor");
        }
    } // superReturnOkInConstructor

    private static Type binopType(final Type left, final Op op, final Type right) throws TypeErrorException {
        final IntType intType = new IntType();
        if (op instanceof PlusOp) {
            // TWO kinds are permitted:
            // int + int: returns int
            // pointer + int: returns same pointer type
            //
            // in both cases, the right side is an int
            typesOK(intType, right);
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
            typesOK(intType, left);
            typesOK(intType, right);
            return intType;
        } else if (op instanceof EqualsOp) {
            // type == type = boolean
            // both need to be of the same type
            typesOK(left, right);
            return new BooleanType();
        } else if (op instanceof LessThanOp) {
        // int < int = boolean
            typesOK(intType, left);
            typesOK(intType, right);
            return new BooleanType();
        } else {
            // should be no other operators
            assert(false);
            throw new TypeErrorException("Unknown operator: " + op.toString());
        }
    } // binopType

    public Type typeofExp(final TypeEnvironment env,
                          final Exp exp) throws TypeErrorException {
        if (exp instanceof NumberExp) {
            return new IntType();
        } else if (exp instanceof BoolExp) {
            return new BooleanType();
        } else if (exp instanceof VariableExp) {
            return env.lookup(((VariableExp)exp).var);
        } else if (exp instanceof BinopExp) {
            // the return type and expected parameter types all depend
            // on the operator.  In all cases, we need to get the types
            // of the operands, and then check if this matches with the
            // operator
            final BinopExp asBinop = (BinopExp)exp;
            final Type leftType = typeofExp(env, asBinop.left);
            final Type rightType = typeofExp(env, asBinop.right);
            return binopType(leftType, asBinop.op, rightType);
        } else if(exp instanceof ClassExp) {
            final ClassExp asClassExp = (ClassExp) exp;
            env.typesInScope(asClassExp.types);
            final ClassDef abstractedClassDef = getClass(asClassExp.name);
            final ClassDef specializedClassDef = TypeRewriter.rewriteClassDef(abstractedClassDef,
                                                                                            asClassExp.types);
            // Check constructor parameters
            checkParameters(env, specializedClassDef.constructor.parameters, asClassExp.parameters);
            return new ClassType(asClassExp.name, asClassExp.types);
        } else if(exp instanceof VarMethodExp) {
            VarMethodExp asMethodExp = (VarMethodExp)exp;
            // Var is defined
            ClassType classType = (ClassType) env.lookup(asMethodExp.var);
            // var has methodname
            MethodDef method = findMethod(classType, asMethodExp.methodName);
            // methodname is public
            if (method.access == new PrivateAccess() && !classType.equals(env.thisType)) {  
                throw new TypeErrorException("Method: " + method.name + " is declared private");
            }
            // Exp's types match up with method's types
            checkParameters(env, method.varDecs, asMethodExp.parameters);
            // return method return type
            return method.returnType;
        } else if(exp instanceof MethodExp) {
            MethodExp asMethodExp = (MethodExp)exp;
            // methodname is within current class
            MethodDef method = findMethod(env.thisType, asMethodExp.methodName);
            // Exps match up with method types
            checkParameters(env, method.varDecs, asMethodExp.parameters);
            // return method return type
            return method.returnType;
        } else {
            assert(false);
            throw new TypeErrorException("Unrecognized expression: " + exp.toString());
        }
    } // typeofExp

    private TypeEnvironment typeCheckBlockStmt(final TypeEnvironment env,
                                                final Type returnType,      // null if return is not ok
                                                final List<VarDec> superParams, // null if not expecting super
                                                final Block stmt)  throws TypeErrorException {
        TypeEnvironment loopEnv = env;
        for (Statement s : stmt.statements) {
            loopEnv = typecheckStatement(loopEnv, returnType, superParams, s);
        }
        return loopEnv;                                             
    }

    public void typecheckSuperStmt(final TypeEnvironment env,
                                    final List<VarDec> superParams,
                                    final SuperStatement stmt) throws TypeErrorException {
        assert(superParams != null);
        checkParameters(env, superParams, stmt.exp);
    } // typecheckSuperStmt

    public void typecheckPrintStmt(final TypeEnvironment env,
                                    final PrintStatement stmt) throws TypeErrorException {
        final Type printType = typeofExp(env, stmt.exp);
        if (!isPrimitive(printType)) {
            throw new TypeErrorException("print can only print primitives; got: " + printType);
        }
    } // typecheckPrintStmt

    public void typecheckReturnExpStmt(TypeEnvironment env, Type returnType, ReturnExpStatement stmt)  throws TypeErrorException {
        // check if types are correct
        typesOK(returnType, typeofExp(env, stmt.exp));
    }

    public void typecheckVarDecAssign(final TypeEnvironment env,
                                      final VarDecAssignment stmt) throws TypeErrorException {
        final Type lhsType = stmt.varDec.type;
        final Type expType = typeofExp(env, stmt.exp);
        typesOK(lhsType, expType);
    } // typecheckAssignStmt

    public void typecheckVarAssign(final TypeEnvironment env,
                                   final VarAssignment stmt) throws TypeErrorException {
        final Type lhsType = env.lookup(stmt.variable);
        final Type expType = typeofExp(env, stmt.exp);
        typesOK(lhsType, expType);
    } // typecheckAssignStmt

    public TypeEnvironment typeCheckWhileStmt(final TypeEnvironment env,
                                            final Type returnType,      // null if return is not ok
                                            final List<VarDec> superParams, // null if not expecting super
                                            final WhileStatement stmt)  throws TypeErrorException {
        Type condition = typeofExp(env, stmt.condition);
        if (condition.equals(new BooleanType())) {
            throw new TypeErrorException("While condition expects boolean type got " + condition);
        }
        // need to save state when entered loop
        boolean prevWhile = env.inWhile;
        // set inwhile to what it was after checking body
        TypeEnvironment afterStatement = typecheckStatement(env, returnType, superParams, stmt);
        afterStatement.inWhile = prevWhile;
        return afterStatement;
        }

    public TypeEnvironment typeCheckIfStmt(final TypeEnvironment env,
                                           final Type returnType,      // null if return is not ok
                                           final List<VarDec> superParams, // null if not expecting super
                                           final IfStatement stmt)  throws TypeErrorException {
        typesOK(new BooleanType(), typeofExp(env, stmt.guard));
        TypeEnvironment trueEnv = typecheckStatement(env, returnType, superParams, stmt.ifTrue);
        TypeEnvironment falseEnv = typecheckStatement(env, returnType, superParams, stmt.ifFalse);
        return env;
    }

    public TypeEnvironment typecheckStatement(final TypeEnvironment env,
                                            final Type returnType,      // null if return is not ok
                                            final List<VarDec> superParams, // null if not expecting super
                                            final Statement stmt) throws TypeErrorException {
        if (stmt instanceof Block) {
            return typeCheckBlockStmt(env, returnType, superParams, (Block)stmt);
        } else if (stmt instanceof ExpStatement) {
            typeofExp(env, ((ExpStatement)stmt).exp);
            return env;
        } else if (stmt instanceof SuperStatement) {
            typecheckSuperStmt(env, superParams, (SuperStatement)stmt);
            return env;
        } else if (stmt instanceof ReturnExpStatement) {
            if(returnType == null) {
                throw new TypeErrorException("Return in super");
            }
            ReturnExpStatement asReturnExp = (ReturnExpStatement)stmt;
            typesOK(returnType, typeofExp(env, asReturnExp.exp));
            return env;
        } else if (stmt instanceof ReturnVoidStatement) {
            if(returnType == null) {
            throw new TypeErrorException("Return in super");
            }
            typesOK(returnType, new VoidType());
            return env;
        } else if (stmt instanceof BreakStatement) {
            if(env.inWhile == false) {
                throw new TypeErrorException("Break outside of loop");
            }
            return env;
        } else if (stmt instanceof PrintStatement) {
            typecheckPrintStmt(env, (PrintStatement)stmt);
            return env;
        } else if (stmt instanceof VarDecAssignment) {
            typecheckVarDecAssign(env, (VarDecAssignment)stmt);
            return env;
        } else if (stmt instanceof VarAssignment) {
            typecheckVarAssign(env, (VarAssignment)stmt);
            return env;
        } else if (stmt instanceof IfStatement) {
            return typeCheckIfStmt(env, returnType, superParams, (IfStatement)stmt);
        } else if (stmt instanceof WhileStatement) {
            return typeCheckWhileStmt(env, returnType, superParams, (WhileStatement)stmt);
        } else {
            assert(false);
            throw new TypeErrorException("Should be unreachable");
        }
    } // typecheckStmt

    public static void typeInScope(final Set<TypeVariable> inScope,
                                   final Type type) throws TypeErrorException {
        if (isPrimitive(type)) {
            // do nothing
        } else if (type instanceof ClassType) {
            final ClassType asClass = (ClassType)type;
            for (final Type curType : asClass.types) {
                typeInScope(inScope, curType);
            }
        } else if (type instanceof TypeVariable) {
            if (!inScope.contains((TypeVariable)type)) {
                throw new TypeErrorException("Type variable not in scope: " + type);
            }
        } else {
            throw new TypeErrorException("Unkown type " + type);
        }
    } // typeInScope

    public List<VarDec> getSuperParams(final ClassType forClass) throws TypeErrorException {
        final ClassDef selfAbstracted = getClass(forClass.name);
        if (selfAbstracted.extendedClass != null) {
            final ClassDef selfSpecialized = TypeRewriter.rewriteClassDef(selfAbstracted,
                                                                                        forClass.types);
            final ClassDef superAbstracted = getClass(selfAbstracted.extendedClass.extendsName);
            final ClassDef superSpecialized = TypeRewriter.rewriteClassDef(superAbstracted,
                                                                                         selfSpecialized.extendedClass.types);
            return superSpecialized.constructor.parameters;
        } else {
            return null;
        }
    } // getSuperParams

    public static void paramsInScope(final Set<TypeVariable> inScope,
                                     final List<VarDec> params) throws TypeErrorException {
        for (final VarDec param : params) {
            typeInScope(inScope, param.type);
        }
    } // paramsInScope

    public static void paramsOk(final Set<TypeVariable> inScope,
                                final List<VarDec> params) throws TypeErrorException {
        noDuplicates(params);
        paramsInScope(inScope, params);
    } // paramsOk

    public void typecheckMethod(final ClassType thisType,
                                final Set<TypeVariable> inScope,
                                final MethodDef methodDef) throws TypeErrorException {
    paramsOk(inScope, methodDef.varDecs);
    typeInScope(inScope, methodDef.returnType);
    superReturnOkInMethod(methodDef.body);
    typecheckStatement(TypeEnvironment.initialEnv(inScope, methodDef.varDecs, thisType),
                        methodDef.returnType,
                        null,
                        methodDef.body);
    } // typecheckMethod

    public void typecheckConstructor(final ClassType thisType,
                                    final Set<TypeVariable> inScopeFromClass,
                                    final Constructor constructor) throws TypeErrorException {
    final ClassDef classDef = getClass(thisType.name);
    paramsOk(inScopeFromClass, constructor.parameters);
    superReturnOkInConstructor(classDef.extendedClass == null, constructor.body);
    typecheckStatement(TypeEnvironment.initialEnv(inScopeFromClass, constructor.parameters, thisType),
                       null,
                       getSuperParams(thisType),
                       constructor.body);
    } // typecheckConstructor

    public void typecheckClass(final ClassName className) throws TypeErrorException {
    final ClassDef classDef = getClass(className);
    final Set<TypeVariable> typeVariablesInScope = asSet(classDef.typeVariables);
    // Check if class extends
    if (classDef.extendedClass != null) {
        // Check if types passed into extended class are defined
        for (final Type type : classDef.extendedClass.types) {
            typeInScope(typeVariablesInScope, type);
        }
    }
    noDuplicateMethodDefs(classDef.methodDefs);
    paramsOk(typeVariablesInScope, classDef.instanceVars);
    instanceVariablesOk(className);
    final ClassType thisType = new ClassType(className, new ArrayList<Type>(classDef.typeVariables));
    typecheckConstructor(thisType, typeVariablesInScope, classDef.constructor);
    for (final MethodDef methodDef : classDef.methodDefs) {
        typecheckMethod(thisType, typeVariablesInScope, methodDef);
    }
    } // typecheckClass 

    public void noCyclicInheritance(final ClassName className) throws TypeErrorException {
            final Set<ClassName> seen = new HashSet<>();
            ClassName current = className;

            while (current != null) {
                if (seen.contains(current)) {
                    throw new TypeErrorException("Cyclic inheritance on " + className);
                }
                seen.add(current);
                final Extends currentExtends = getClass(current).extendedClass;
                current = (currentExtends == null) ? null : currentExtends.extendsName;
            }
    } // noCyclicInheritance

    public void typecheckClasses() throws TypeErrorException {
    // cyclic checks go first, as all downstream code assumes acyclic
    // inheritance
    for (final ClassName className : classes.keySet()) {
        noCyclicInheritance(className);
    }
    for (final ClassName className : classes.keySet()) {
        typecheckClass(className);
    }
    } // typecheckClasses

    public static Map<ClassName, ClassDef> classMapping(final List<ClassDef> classes) throws TypeErrorException {
    final Map<ClassName, ClassDef> mapping = new HashMap<>();
    for (final ClassDef classDef : classes) {
        if (mapping.containsKey(classDef.name)) {
            throw new TypeErrorException("Duplicate class name: " + classDef.name);
        }
        mapping.put(classDef.name, classDef);
    }
    return mapping;
    } // classMapping

    // Called in testing to type check the given program
    public static void typecheckProgram(final Program program) throws TypeErrorException {
    final TypeChecker typeChecker =  new TypeChecker(classMapping(program.classDefs));
    typeChecker.typecheckClasses();
    typeChecker.typecheckStatement(TypeEnvironment.initialEnv(new ArrayList<TypeVariable>(), new ArrayList<VarDec>(), null),
                                    null,
                                    null,
                                    program.statement);
    }
}