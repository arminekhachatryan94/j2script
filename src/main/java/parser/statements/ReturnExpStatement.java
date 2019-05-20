package j2script.statements;
import j2script.expressions.Exp;


public class ReturnExpStatement implements Statement {
    public final Exp exp;

    public ReturnExpStatement(final Exp exp) {
        this.exp = exp;
    }

    public int hashCode() { return exp.hashCode(); }

    public boolean equals(final Object other) {
        return (other instanceof ReturnExpStatement &&
                ((ReturnExpStatement)other).exp.equals(exp));
    }

    public String toString() {
        return "return " + exp.toString();
    }
}