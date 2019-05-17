package j2script.tokens;

public class GreaterThanToken implements Token {
    public int hashCode() {
        return 13;
    }

    public boolean equals(Object obj) {
        return obj instanceof GreaterThanToken;
    }

    public String toString() {
        return ">";
    }
}
