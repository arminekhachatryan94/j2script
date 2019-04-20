package j2script.operators;

public class EqualsOp implements Op {
    // public int hashCode() { return 4; }
    public boolean equals(final Object other) {
        return other instanceof EqualsOp;
    }
    public String toString() { return "=="; }
}