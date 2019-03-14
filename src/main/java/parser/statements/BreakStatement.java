package j2script.statements;

public class BreakStatement implements Statement {
    public int hashCode() { return 0; }
    public boolean equals(final Object other) {
        return other instanceof BreakStatement;
    }
    public String toString() { return "break"; }
}