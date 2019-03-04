package j2script.expressions;

import j2script.access.Access;

public class InstanceDec {
    public final Access access;
    public final VarDec vardec;

    public InstanceDecExp(final Access access, final VarDec vardec) {
        this.access = access;
        this.vardec = vardec;
    }

    public int hashCode() {
        return access.hashCode() + vardec.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof InstanceDec) {
            final InstanceDec otherProgram = (InstanceDec) other;
            return otherProgram.access.equals(access) &&
                   otherProgram.vardec.equals(vardec);
        } else {
            return false;
        }
    }

    public String toString() {
        return access.toString() + " " + vardec.toString();
    }
}