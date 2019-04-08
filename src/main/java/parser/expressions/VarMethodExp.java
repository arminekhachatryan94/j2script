package j2script.expressions;

import j2script.names.Variable;
import j2script.names.MethodName;

import java.util.Arrays;

public class VarMethodExp implements Exp {
    public final Variable var;
    public final MethodName methodName;
    public final Exp[] parameters;

    public VarMethodExp(final Variable var,
                    final MethodName methodName,
                    final Exp[] parameters){
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
                    Arrays.deepEquals(parameters,
                                      otherExp.parameters));
        } else {
            return false;
        }
    }
    
    public String toString() {
        return (var.toString() + "(" +
                methodName.toString() + ")");
    }

    public String emit() {
        return "";
    }
}