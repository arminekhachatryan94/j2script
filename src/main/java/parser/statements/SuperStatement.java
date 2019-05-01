package j2script.statements;

import j2script.expressions.Exp;

import java.util.List;

public class SuperStatement implements Statement {
    public final List<Exp> exp;

    public SuperStatement(List<Exp> exp) {
      this.exp = exp;
    }

    public int hashCode() { return 0; }
    public boolean equals(final Object other) {
        return other instanceof SuperStatement;
    }
    public String toString() { return "super()"; }
}