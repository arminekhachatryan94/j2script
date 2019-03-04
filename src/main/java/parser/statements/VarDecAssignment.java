package j2script.statements;

public class VarDecAssignment implements Statement {
    public final VarDec varDec;
    public final Exp exp;

    public VarDecAssignment(final VarDec varDec, final Exp exp) {
        this.varDec = varDec;
        this.exp = exp;
    }

    public int hashCode() {
        return varDec.hashCode() + exp.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof VarDecAssignment) {
            final VarDecAssignment otherAssignment = (VarDecAssignment) other;
            return otherAssignment.varDec.equals(varDec) &&
                   otherAssignment.exp.equals(exp);
        } else {
            return false;
        }
    }

    public String toString() {
        return (varDec.toString() + " = " + exp.toString());
    }
}