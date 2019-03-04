package j2script.declarations;

import j2script.types.Type;
import j2script.names.Variable;

public class VarDec {
    public final Type type;
    public final Variable var;

    public VarDec(final Type type, final Variable var) {
        this.type = type;
        this.var = var;
    }

    public int hashCode() {
        return type.hashCode() + var.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof VarDec) {
            final VarDec otherProgram = (VarDec) other;
            return otherProgram.type.equals(type) &&
                   otherProgram.var.equals(var);
        } else {
            return false;
        }
    }

    public String toString() {
        return type.toString() + " " + var.toString();
    }
}