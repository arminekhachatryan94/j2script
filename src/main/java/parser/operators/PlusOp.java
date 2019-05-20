package j2script.operators;

public class PlusOp implements Op {
    public boolean equals(final Object other) {
        return other instanceof PlusOp;
    }
    public String toString() {
        return "+";
    }
}