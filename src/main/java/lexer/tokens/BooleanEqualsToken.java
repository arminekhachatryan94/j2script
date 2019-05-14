package j2script.tokens;

public class BooleanEqualsToken implements Token {
    public int hashCode() {
        return 55;
    }

    public boolean equals(Object obj) {
        return obj instanceof BooleanEqualsToken;
    }

    public String toString() {
        return "==";
    }
}
