package j2script.tokens;

public class WhileToken implements Token {
    public int hashCode() {
        return 10000;
    }

    public boolean equals(Object obj) {
        return obj instanceof WhileToken;
    }

    public String toString() {
        return "Boolean";
    }
}