package j2script.tokens;

public class LeftCurlyToken implements Token {
    public int hashCode() {
        return 10000;
    }

    public boolean equals(Object obj) {
        return obj instanceof LeftCurlyToken;
    }

    public String toString() {
        return "Boolean";
    }
}