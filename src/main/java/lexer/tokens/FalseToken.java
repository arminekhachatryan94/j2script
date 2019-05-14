package j2script.tokens;

public class FalseToken implements Token {
    public int hashCode() {
        return 54;
    }

    public boolean equals(Object obj) {
        return obj instanceof FalseToken;
    }

    public String toString() {
        return "false";
    }
}
