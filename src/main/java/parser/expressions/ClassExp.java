package j2script.expressions;
import j2script.names.*;

public class ClassExp implements Exp {
    public final Classname name;
    public final Exp [] parameters;

    public ClassExp(final Classname name,
                    final Exp[] parameters){
        this.name=name;
        this.parameters = parameters;
    }
    public int hashCode() {
        return name.hashCode() + Arrays.deepHashCode(parameters);
    }

    public boolean equals(final Object other) {
        if (other instanceof ClassExp) {
            final ClassExp otherExp = (ClassExp)other;
            return (otherExp.name.equals(name) &&
                    Arrays.deepEquals(parameters,
                                      otherExp.parameters));
        } else {
            return false;
        }
    }
    
    public String toString() {
        return (name.toString() + "(" +
                Join.join(", ", parameters) + ")");
    }
}