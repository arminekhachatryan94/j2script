package j2script.statements;

import j2script.expressions.Exp;

public class PrintStatement{
    public final Exp exp;

    public PrintStatement(Exp exp) {
      this.exp = exp;
    }
}