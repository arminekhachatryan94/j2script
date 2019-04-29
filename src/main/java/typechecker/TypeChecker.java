package j2script;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
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
  private final Map<ClassName, ClassDef> classes;

  private TypeChecker(final Program program) throws TypeErrorException {
    this.classes = classes;
  }

  public ClassDef getClass(final ClassName name) throws TypeErrorException {
    final ClassDef result = classes.get(name);
    if (result == null) {
      throw new TypeErrorException("No such class defined: " + name);
    } else {
      return result;
    }
  } // getClass

  public boolean isPrimitive(Type type) {
    return type.equals(new IntType()) || 
           type.equals(new BooleanType()) ||
           type.equals(new StringType());
  }

  // Check for redefined instance vars from parent later
  public static void noDuplicates(final List<VarDec> params) throws TypeErrorException {
    final Set<Variable> seen = new HashSet<>();
    for (final VarDec current : params) {
      if (seen.contains(current.variable)) {
        throw new TypeErrorException("Duplicate variable: " + current.variable);
      }
      seen.add(current.variable);
    }
  } // noDuplicates

  // Checks to see if there is a duplicate method defined
  // Not checking names since overloaded methods are allowed
  public static void noDuplicateMethodDefs(final List<MethodDef> methods) throws TypeErrorException {
    final List<MethodDef> seen = new ArrayList<MethodDef>();
      for (int i = 0; i < methods.size(); i++) {
        for(int j = i+1; j < methods.size(); j++) {
          if(methods[i] != methods[j]) {
            // check if same
            if(methods[i].name == methods[j].name && 
               Arrays.deepEquals(methods[i].varDecs, methods[j].varDecs)) {
                 throw new TypeErrorException("Duplicate method defnied: " + method.name);
               }
          }
        }
      }
  } // noDuplicateMethodDefs

  // checks that subclasses don't redefined parent class instance variables
  public static void instanceVariablesOk(final Set<Variable> seen, final ClassName current) throws TypeErrorException {
    if (current != null) {
      final ClassDefinition classDef = getClass(current);
      for (final VarDec param : classDef.instanceVariables) {
        if (seen.contains(param.variable)) {
          throw new TypeErrorException("Instance variable " + param.var + " redefined in " + ": " + current.name);
        }
        seen.add(param.variable);
      }
      instanceVariablesOk(seen, classDef.extendedClass);
      }
  } // instanceVariablesOk

  // checks that subclasses don't redefined parent class instance variables
  public static void instanceVariablesOk(final ClassName current) throws TypeErrorException {
    instanceVariablesOk(new HashSet<Variable>(), current);
  } // instanceVariablesOk

  public static boolean containsNoReturns(final List<Stmt> statements) {
    for (int i = 1; i < statements.size(); i++) {
      if (stmt instanceof ReturnStmt) {
        return false;
      }
    }
    return true;
  } // containsNoReturns

  public static boolean containsNoSupers(final List<Stmt> statements) {
    for (final Stmt stmt : statements) {
      if (stmt instanceof SuperStatement) {
        return false;
      }
    }
    return true;
  } // containsNoSupers
  
  //  Checks if super call is OK
  public static void superReturnOkInConstructor(final boolean isBaseClass,
                                                final Stmt stmt) throws TypeErrorException {
    final List<Stmt> statements;
    if (stmt instanceof Block) {
      final List<Stmt> statements = stmt.statements;
    } else {
      statements.add(stmt);
    }
    
    // is baseclass and contains super
    if (isBaseClass && !containsNoSupers(statements)) {
      throw new TypeErrorException("base classes cannot contain super");
    }
    // is not baseclass and has no statements or the first statement is not super
    if (!isBaseClass &&
      (statements.size() == 0 || !(statements.get(0) instanceof SuperStmt))) {
      throw new TypeErrorException("super needs to be first in subclass constructor");
    }
    // constructor cannot have return
    if (!containsNoReturns(statements)) {
      throw new TypeErrorException("return in constructor");
    }
  } // superReturnOkInConstructor

  // Checks if class types are comparable
  public void typesOk(final Type baseType, final Type subType) throws TypeErrorException {
    if (!baseType.equals(subType)) {
      // see if subType is a subtype of base type
      if (baseType instanceof ClassType && subType instanceof ClassType) {
        final ClassName subName = ((ClassType)subType).name;
        final ClassDefinition subClassDef = getClass(subName);
        if (subClassDef.extendedClass != null) {
          typesOk(baseType, new ClassType(subClassDef.extendedClass));
        } else {
          throw new TypeErrorException(subType.toString() + " is not a subtype of " + baseType.toString());
        }
      } else {
        throw new TypeErrorException("Base type " + baseType + " is not comparable to " + subType);
      }
    }
  } // typesOk

  public Type typeofExp(final TypeEnvironment env,
                        final Exp exp) throws TypeErrorException {
    if (exp instanceof IntExp) {
      return new IntType();
    } else if (exp instanceof LhsExp) {
      return typeofLhs(env, ((LhsExp)exp).lhs);
    } else {
      return deadCode();
    }
  } // typeofExp

  private static TypeEnvironment typeCheckBlockStmt(final TypeEnvironment env,
                                                    final Type returnType,      // null if return is not ok
                                                    final List<VarDec> superParams, // null if not expecting super
                                                    final Stmt stmt) {
    for (Statement s : block.statements) {
      typecheckStatement(env, returnType, superParams, s);
    }
  }

  public void typecheckPrintStmt(final TypeEnvironment env,
                                 final PrintStmt stmt) throws TypeErrorException {
    final Type printType = typeofExp(env, stmt.exp);
    if (!isPrimitive(printType)) {
        throw new TypeErrorException("print can only print primitives; got: " + printType);
    }
  } // typecheckPrintStmt

  public void typecheckReturnExpStmt(TypeEnvironment env, Type returnType, ReturnExpStmt stmt) {
    assert(returnType != null);
    typesOk(returnType, typeofExp(env, stmt.exp));
  }

  public void typecheckVarDecAssign(final TypeEnvironment env,
                                  final VarDecAssignement stmt) throws TypeErrorException {
    final Type lhsType = stmt.varDec.type;
    final Type expType = typeofExp(env, stmt.exp);
    typesOk(lhsType, expType);
  } // typecheckAssignStmt

  public void typecheckVarAssign(final TypeEnvironment env,
                                final VarAssignement stmt) throws TypeErrorException {
    final Type lhsType = env.lookup(stmt.variable);
    final Type expType = typeofExp(env, stmt.exp);
    typesOk(lhsType, expType);
  } // typecheckAssignStmt

  public static TypeEnvironment typeCheckWhileStmt(final TypeEnvironment env,
                                                   final Type returnType,      // null if return is not ok
                                                   final List<VarDec> superParams, // null if not expecting super
                                                   final Stmt stmt) {
    Type condition = typeofExp(stmt.condition);
    if (condition.equals(new BooleanType()) {
      throw new TypeErrorException("While condition expects boolean type got " + condition);
    }
    env.inWhile = true;
    return typecheckStatement(env, returnType, superParams, stmt);
  }

  public static TypeEnvironment typeCheckIfStmt(final TypeEnvironment env,
                                                final Type returnType,      // null if return is not ok
                                                final List<VarDec> superParams, // null if not expecting super
                                                final Stmt stmt) {
    Type guard = typeofExp(stmt.guard);
    if (guard.equals(new BooleanType()) {
      throw new TypeErrorException("If condition expects boolean type got " + guard);
    }
    TypeEnvironment trueEnv = typecheckStatement(env, returnType, superParams, stmt.ifTrue);      // STOPPED HERE!!!!
    TypeEnvironment falseEnv = typecheckStatement(env, returnType, superParams, stmt.ifFalse);   // Need help here
  }

  public TypeEnvironment typecheckStmt(final TypeEnvironment env,
                                       final Type returnType,      // null if return is not ok
                                       final List<VarDec> superParams, // null if not expecting super
                                       final Stmt stmt) throws TypeErrorException {
  if (stmt instanceof Block) {
    return typeCheckBlockStmt(env, returnType, superParams, (Block)stmt);
  } else if (stmt instanceof ExpStatement) {
    typeofExp(env, stmt.exp);
    return env;
  } else if (stmt instanceof ReturnExpStatement) {
    typecheckReturnExpStmt(env, returnType, (ReturnExpStmt)stmt);
    return env;
  } else if (stmt instanceof ReturnVoidStatement) {
    assert(returnType != null);
    return env;
  } else if (stmt instanceof BreakStatement) {
    if(env.inWhile == false) {
      throw new TypeErrorException("Break outside of loop");
    }
    env.inWhile = false;
    return env;
  } else if (stmt instanceof PrintStmt) {
    typecheckPrintStmt(env, (PrintStmt)stmt);
    return env;
  } else if (stmt instanceof VarDecAssignement) {
    typecheckVarDecAssign(env, (VarDecAssignement)stmt);
    return env;
  } else if (stmt instanceof VarAssignement) {
    typecheckVarAssign(env, (VarAssignement)stmt);
    return env;
  } else if (stmt instanceof WhileStatement) {
    return typeCheckWhileStmt(env, returnType, superParams, (WhileStatement)stmt);
  } else if (stmt instanceof IfStatement) {
    return typeCheckIfStmt(env, returnType, superParams, (IfStatement)stmt);
  } else {
      return deadCode();
  }
  } // typecheckStmt

  public List<VarDec> getSuperParams(final ClassName forClass) throws TypeErrorException {
    final ClassDef classDef = getClass(forClass);
    if (classDef.extendsName != null) {
      return getClass(classDef.extendsName).constructor.parameters;
    } else {
      return null;
    }
  } // getSuperParams

  public void typecheckConstructor(final ClassName onClass,
                                   final Constructor constructor) throws TypeErrorException {
    final ClassDefinition classDef = getClass(onClass);
    noDuplicates(constructor.parameters);
    superReturnOkInConstructor(classDef.extendsName == null, constructor.body);
    typecheckStmt(TypeEnvironment.initialEnv(constructor.parameters, onClass),
                  null,
                  getSuperParams(onClass),
                  constructor.body);
  } // typecheckConstructor

  public void typecheckClass(final ClassName className) throws TypeErrorException {
    final ClassDef classDef = getClass(className);
    noDuplicateMethodDefs(classDef.methodDefs);
    noDuplicates(classDef.instanceVars);
    instanceVariablesOk(className);
    typecheckConstructor(className, classDef.constructor);
    for (final MethodDefinition methodDef : classDef.methods) {
        typecheckMethod(className, methodDef);
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
      current = getClass(current).extendedClass;
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
        if (mapping.containsKey(classDef.myName)) {
            throw new TypeErrorException("Duplicate class name: " + classDef.myName);
        }
        mapping.put(classDef.myName, classDef);
    }
    return mapping;
  } // classMapping

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
    final TypeChecker typeChecker =  new TypeChecker(classMapping(program.classDefs));
    typeChecker.typecheckClasses();
    typeChecker.typecheckStatement(new InScope(new VoidType(),
                                              new HashMap<Variable, Type>(),
                                              false);)
    statementScope.typecheckStatement(program.statement);
  }
}