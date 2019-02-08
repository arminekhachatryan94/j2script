package j2script.tokens;

public class EqualToken implements Token {
    public int hashCode() {
        return 10000;
    }

    public boolean equals(Object obj) {
        return obj instanceof EqualToken;
    }

    public String toString() {
        return "Boolean";
    }
}