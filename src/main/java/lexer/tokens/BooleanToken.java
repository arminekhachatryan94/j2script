package j2script.tokens;
public class BooleanToken implements Token {
    public int hashCode() {
        return 3;
    }

    public boolean equals(Object obj) {
        return obj instanceof BooleanToken;
    }

    public String toString() {
        return "boolean";
    }
}
