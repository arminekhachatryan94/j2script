package j2script.statements;

import j2script.expressions.Exp;

public class PrintStatement implements Statement{
    public final Exp exp;

    public PrintStatement(Exp exp) {
      this.exp = exp;
    }
    public int hashCode() { return exp.hashCode(); }
    public boolean equals(final Object other) {
        return other instanceof PrintStatement;
    }
    public String toString() { return exp.toString() + ""; }
}