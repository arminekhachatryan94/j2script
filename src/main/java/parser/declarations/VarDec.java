package j2script.expressions;

import j2script.types.Type;

public class VarDec {
    public final Type type;
    public final String name;

    public VarDec(final Type type, final String name) {
        this.type = type;
        this.name = name;
    }

    public int hashCode() {
        return type.hashCode() + vardec.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof VarDec) {
            final InstanceDec otherProgram = (VarDec) other;
            return otherProgram.type.equals(type) &&
                   otherProgram.name.equals(name);
        } else {
            return false;
        }
    }

    public String toString() {
        return type.toString() + " " + name.toString();
    }
}