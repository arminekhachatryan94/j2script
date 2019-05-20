package j2script.operators;

public class MultOp implements Op {
    public boolean equals(final Object other) {
        return other instanceof MultOp;
    }
    public String toString() {
        return "*";
    }
}