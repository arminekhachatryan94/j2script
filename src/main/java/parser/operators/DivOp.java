package j2script.operators;

public class DivOp implements Op {
    public boolean equals(final Object other) {
        return other instanceof DivOp;
    }
    public String toString() {
        return "/";
    }
}