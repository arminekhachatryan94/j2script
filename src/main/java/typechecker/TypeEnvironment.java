package j2script;

import j2script.TypeChecker;
import j2script.declarations.VarDec;
import j2script.names.Variable;
import j2script.types.Type;
import j2script.types.ClassType;
import j2script.types.TypeVariable;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class TypeEnvironment {
    private final Set<TypeVariable> inScope;     // type variables in scope
    private final Map<Variable, Type> variables;
    public final Set<Variable> defined;
    public final ClassType thisType; // null if outside of method
    public boolean inWhile;
    
    public TypeEnvironment(final Set<TypeVariable> inScope,
                           final Map<Variable, Type> variables,
                           final ClassType thisType,
                           final Set<Variable> defined,
                           boolean inWhile) {
        this.inScope = inScope;
        this.variables = variables;
        this.thisType = thisType;
        this.defined = defined;
        this.inWhile = inWhile;
    }

    public void typeInScope(final Type type) throws TypeErrorException {
        TypeChecker.typeInScope(inScope, type);
    }

    public void typesInScope(final List<Type> types) throws TypeErrorException {
        for (final Type type : types) {
            typeInScope(type);
        }
    }
    
    public Type lookup(final Variable variable) throws TypeErrorException {
        final Type result = variables.get(variable);
        if (result == null) {
            throw new TypeErrorException("No such variable: " + variable);
        } else {
            return result;
        }
    }

    public TypeEnvironment addVariable(final Variable variable,
                                       final Type type) throws TypeErrorException {
        if (!variables.containsKey(variable)) {
            final Map<Variable, Type> newVariables = new HashMap<Variable, Type>(variables);
            newVariables.put(variable, type);
            return new TypeEnvironment(inScope, newVariables, thisType, defined, inWhile);
        } else {
            throw new TypeErrorException("Redefinition of variable: " + variable);
        }
    }

    public TypeEnvironment addVariable(final VarDec vardec) throws TypeErrorException {
        return addVariable(vardec.var, vardec.type);
    }

    public boolean checkDefined(Variable var) {
        return defined.contains(var);
    }

    public TypeEnvironment addDefinedVariable(final Variable variable) throws TypeErrorException {
        final Set<Variable> newdefined = new HashSet<>(defined);
        newdefined.add(variable);
        return new TypeEnvironment(inScope, variables, thisType, newdefined, inWhile);
    }

    public TypeEnvironment removeDefinedVariable(final Variable variable) throws TypeErrorException {
        final Set<Variable> newdefined = new HashSet<>(defined);
        newdefined.remove(variable);
        return new TypeEnvironment(inScope, variables, thisType, newdefined, inWhile);
    }

    public static Map<Variable, Type> variableMapping(final List<VarDec> params) throws TypeErrorException {
        TypeChecker.noDuplicates(params);
        final Map<Variable, Type> result = new HashMap<Variable, Type>();
        for (final VarDec param : params) {
            result.put(param.var, param.type);
        }
        return result;
    } // variableMapping

    public static TypeEnvironment initialEnv(final Set<TypeVariable> inScope,
                                             final List<VarDec> params,
                                             final ClassType thisType) throws TypeErrorException {
        return new TypeEnvironment(inScope, variableMapping(params), thisType, new HashSet<>(), false);
    } // initialEnv

    public static TypeEnvironment initialEnv(final List<TypeVariable> typeVariables,
                                             final List<VarDec> params,
                                             final ClassType thisType) throws TypeErrorException {
        return initialEnv(TypeChecker.asSet(typeVariables),
                          params,
                          thisType);
    } // initialEnv
}
