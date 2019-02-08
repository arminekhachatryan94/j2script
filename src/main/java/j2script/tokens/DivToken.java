package j2script.tokens;

public class DivToken implements Token {
    public int hashCode() {
        return 1;
    }

    public boolean equals(Object obj) {
        return obj instanceof DivToken;
    }

    public String toString() {
        return "Boolean";
    }
}