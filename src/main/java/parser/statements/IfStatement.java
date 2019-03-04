package j2script.expressions;

public class IfStatement implements Statement {
    public final Exp guard;
    public final Statement ifTrue;
    public final Statement ifFalse;

    public IfExp(final Exp guard,
                 final Statement ifTrue,
                 final Statement ifFalse) {
        this.guard = guard;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    public int hashCode() {
        return guard.hashCode() + ifTrue.hashCode() +  ifFalse.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof IfStatement) {
            final IfStatement otherif = (IfStatement) other;
            return otherif.guard.equals(guard) &&
                   otherif.ifTrue.equals(ifTrue) &&
                   otherif.ifFalse.equals(ifFalse);
        } else {
            return false;
        }
    }

    public String toString() {
        return ("if(" + guard.toString() + ")");
    }
}