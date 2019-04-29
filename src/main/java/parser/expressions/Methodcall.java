package j2script.expressions;

import j2script.names.Variable;
import j2script.names.MethodName;

import java.util.List;


public class Methodcall implements Exp {
    public final MethodName methodName;
    public final List<Exp> parameters;

    public Methodcall(final MethodName methodName,
                    final List<Exp> parameters){
        this.methodName=methodName;
        this.parameters = parameters;
    }
    public int hashCode() {
        return methodName.hashCode();
    }

    // public boolean equals(final Object other) {
    //     if (other instanceof Methodcall) {
    //         final Methodcall otherExp = (Methodcall)other;
    //         return (otherExp.equals(var));
    //     } else {
    //         return false;
    //     }
    // }
    
    public String toString() {
        return ("(" +
                methodName.toString() + ")");
    }
    public String emit() {
        return ("(" +
                methodName.toString() + ")");
    }
}