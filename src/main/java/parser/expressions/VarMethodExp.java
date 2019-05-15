package j2script.expressions;

import j2script.names.Variable;
import j2script.names.MethodName;

import java.util.Arrays;
import java.util.List;

public class VarMethodExp implements Exp {
    public final Variable var;
    public final MethodName methodName;
    public final List<Exp> parameters;

    public VarMethodExp(final Variable var,
                        final MethodName methodName,
                        final List<Exp> parameters){
        this.var=var;
        this.methodName=methodName;
        this.parameters = parameters;
    }
    public int hashCode() {
        return var.hashCode() + methodName.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof VarMethodExp) {
            final VarMethodExp otherExp = (VarMethodExp)other;
            return (otherExp.var.equals(var) &&
                    Arrays.deepEquals(parameters.toArray(),
                                      otherExp.parameters.toArray()));
        } else {
            return false;
        }
    }
    
    public String toString() {
        String exp = methodName + "(";
        for(int i = 0; i < parameters.size(); i++) {
            exp += parameters.get(i).emit();
            if(i+1 < parameters.size()) {
                exp += ", ";
            }
        }
        exp += ")";
        return exp;
    }

    public String emit() {
        String exp = methodName + "(";
        for(int i = 0; i < parameters.size(); i++) {
            exp += parameters.get(i).emit();
            if(i+1 < parameters.size()) {
                exp += ", ";
            }
        }
        exp += ")";
        return exp;
    }
}