package j2script.statements;

import j2script.expressions.Exp;

public class WhileStatement implements Statement {
    public final Exp condition;
    public final Statement stmt;


    public WhileStatement(final Exp condition,
                    final Statement stmt) {
        this.condition = condition;
        this.stmt = stmt;
    }

    public int hashCode() {
        return condition.hashCode() + stmt.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof WhileStatement) {
            final WhileStatement otherWhile = (WhileStatement) other;
            return otherWhile.condition.equals(condition) &&
                   otherWhile.stmt.equals(stmt);
        } else {
            return false;
        }
    }

    public String toString() {
        return ("while(" + condition.toString() + ")");
    }

    public String emit() {
        return ("while(" + condition.toString() + ")");
    }
}