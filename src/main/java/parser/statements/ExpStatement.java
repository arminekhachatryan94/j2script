package j2script.statements;

import j2script.expressions.Exp;

public class ExpStatement implements Statement {
    public final Exp exp;

    public ExpStatement(Exp exp) {
      this.exp = exp;
    }

    public int hashCode() { return 0; }
    public boolean equals(final Object other) {
        return other instanceof ExpStatement;
    }
    public String toString() { return "Expression"; }
}