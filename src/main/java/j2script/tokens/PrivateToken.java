package j2script.tokens;

public class PrivateToken implements Token {
    public int hashCode() {
        return 10000;
    }

    public boolean equals(Object obj) {
        return obj instanceof PrivateToken;
    }

    public String toString() {
        return "Boolean";
    }
}