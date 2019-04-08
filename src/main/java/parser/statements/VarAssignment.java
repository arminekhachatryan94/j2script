package j2script.statements;

import j2script.names.Variable;
import j2script.expressions.Exp;

public class VarAssignment implements Statement {
    public final Variable variable;
    public final Exp exp;

    public VarAssignment(final Variable variable, final Exp exp) {
        this.variable = variable;
        this.exp = exp;
    }

    public int hashCode() {
        return variable.hashCode() + exp.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof VarAssignment) {
            final VarAssignment otherAssignment = (VarAssignment) other;
            return otherAssignment.variable.equals(variable) &&
                   otherAssignment.exp.equals(exp);
        } else {
            return false;
        }
    }

    public String toString() {
        return (variable.toString() + " = " + exp.toString());
    }
}