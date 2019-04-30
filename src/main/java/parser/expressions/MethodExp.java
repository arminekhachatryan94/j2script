package j2script.expressions;

import j2script.names.MethodName;

import java.util.Arrays;
import java.util.List;

public class MethodExp implements Exp {
    public final MethodName methodName;
    public final List<Exp> parameters;

    public MethodExp(final MethodName methodName,
                     final List<Exp> parameters){
        this.methodName=methodName;
        this.parameters = parameters;
    }
    public int hashCode() {
        return methodName.hashCode() + Arrays.deepHashCode(parameters.toArray());
    }

    public boolean equals(final Object other) {
        if (other instanceof MethodExp) {
            final MethodExp otherExp = (MethodExp)other;
            return (otherExp.methodName.equals(methodName) &&
                    Arrays.deepEquals(parameters.toArray(),
                                      otherExp.parameters.toArray()));
        } else {
            return false;
        }
    }
    
    public String toString() {
        return methodName.toString();
    }
}