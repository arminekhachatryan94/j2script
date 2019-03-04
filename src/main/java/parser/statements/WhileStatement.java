package j2script.statements;

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
            return otherif.condition.equals(condition) &&
                   otherif.stmt.equals(stmt);
        } else {
            return false;
        }
    }

    public String toString() {
        return ("while(" + condition.toString() + ")");
    }
}