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

public class TypeEnvironment {
    private final Set<TypeVariable> inScope;     // type variables in scope
    private final Map<Variable, Type> variables;
    public final ClassType thisType; // null if outside of method
    public boolean inWhile;
    
    public TypeEnvironment(final Set<TypeVariable> inScope,
                           final Map<Variable, Type> variables,
                           final ClassType thisType,
                           boolean inWhile) {
        this.inScope = inScope;
        this.variables = variables;
        this.thisType = thisType;
        this.inWhile = inWhile;
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
            return new TypeEnvironment(inScope, newVariables, thisType, inWhile);
        } else {
            throw new TypeErrorException("Redefinition of variable: " + variable);
        }
    }

    public TypeEnvironment addVariable(final VarDec vardec) throws TypeErrorException {
        return addVariable(vardec.var, vardec.type);
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
        return new TypeEnvironment(inScope, variableMapping(params), thisType, false);
    } // initialEnv

    public static TypeEnvironment initialEnv(final List<TypeVariable> typeVariables,
                                             final List<VarDec> params,
                                             final ClassType thisType) throws TypeErrorException {
        return initialEnv(TypeChecker.asSet(typeVariables),
                          params,
                          thisType);
    } // initialEnv
}
