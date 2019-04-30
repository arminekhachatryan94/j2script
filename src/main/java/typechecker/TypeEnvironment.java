package j2script;

import j2script.TypeChecker;
import j2script.declarations.VarDec;
import j2script.names.ClassName;
import j2script.names.Variable;
import j2script.types.Type;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class TypeEnvironment {
    private final Map<Variable, Type> variables;
    public final ClassName thisClass; // null if outside of method
    public boolean inWhile;
    
    public TypeEnvironment(final Map<Variable, Type> variables,
                           final ClassName thisClass,
                           boolean inWhile) {
        this.variables = variables;
        this.thisClass = thisClass;
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
            return new TypeEnvironment(newVariables, thisClass, inWhile);
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

    public static TypeEnvironment initialEnv(final List<VarDec> params,
                                             final ClassName onClass) throws TypeErrorException {
        return new TypeEnvironment(variableMapping(params), onClass, false);
    } // initialEnv
}
