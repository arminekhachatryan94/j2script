package j2script.expressions;
import j2script.names.*;

public class VarMethodExp implements Exp {
    public final Variable var;
    public final Methodname methodname;
    public final Exp [] parameters;

    public ClassExp(final Variable var,
                    final Methodname methodname,
                    final Exp[] parameters){
        this.var=var;
        this.methodname=methodname;
        this.parameters = parameters;
    }
    public int hashCode() {
        return var.hashCode() + methodname.deepHashCode(parameters);
    }

    public boolean equals(final Object other) {
        if (other instanceof ClassExp) {
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
                Join.join(", ", methodname) + ")");
    }
}