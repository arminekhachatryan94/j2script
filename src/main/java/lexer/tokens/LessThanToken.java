package j2script.tokens;

public class LessThanToken implements Token {
    public int hashCode() {
        return 51;
    }

    public boolean equals(Object obj) {
        return obj instanceof LessThanToken;
    }

    public String toString() {
        return "<";
    }
}
