package j2script.statements;

public class ReturnVoidStatement implements Statement {
    public int hashCode() { return 2; }
    public boolean equals(final Object other) {
        return other instanceof ReturnVoidStatement;
    }
    public String toString() { return "return"; }
}