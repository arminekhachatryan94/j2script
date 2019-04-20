package j2script.operators;

public class LessThanOp implements Op {
    // public int hashCode() { return 5; }
    public boolean equals(final Object other) {
        return other instanceof LessThanOp;
    }
    public String toString() { return "<"; }
}