package j2script.typechecker;

import java.util.Map;
import java.util.HashMap;

public class TypeEnvironment {
    private final Map<Variable, Type> variables;
    private final ClassName thisClass; // null if outside of method
    public boolean inWhile;
    
    public TypeEnvironment(final Map<Variable, Type> variables,
                           final ClassName thisClass,
                           boolean inWhile) {
        this.variables = variables;
        this.thisClass = thisClass;
        this.inWhile = inWhile;
    }

    public Type thisType() throws TypeErrorException {
        if (thisClass == null) {
            throw new TypeErrorException("this used outside of class");
        } else {
            return new ClassType(thisClass);
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
            return new TypeEnvironment(newVariables, thisClass);
        } else {
            throw new TypeErrorException("Redefinition of variable: " + variable);
        }
    }

    public TypeEnvironment addVariable(final VarDec vardec) throws TypeErrorException {
        return addVariable(vardec.variable, vardec.type);
    }

    public static Map<Variable, Type> variableMapping(final VarDec[] params) throws TypeErrorException {
        Typechecker.noDuplicates(params);
        final Map<Variable, Type> result = new HashMap<Variable, Type>();
        for (final VarDec param : params) {
            result.put(param.variable, param.type);
        }
        return result;
    } // variableMapping

    public static TypeEnvironment initialEnv(final VarDec[] params,
                                             final ClassName onClass) throws TypeErrorException {
        return new TypeEnvironment(variableMapping(params), onClass);
    } // initialEnv
}
