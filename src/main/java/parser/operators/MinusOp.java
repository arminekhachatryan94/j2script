package j2script.operators;

public class MinusOp implements Op {
    public boolean equals(final Object other) {
        return other instanceof MinusOp;
    }
    public String toString() {
        return "-";
    }
}