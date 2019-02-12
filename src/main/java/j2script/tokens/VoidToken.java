package j2script.tokens;

public class VoidToken implements Token {
    public int hashCode() {
        return 24;
    }

    public boolean equals(Object obj) {
        return obj instanceof VoidToken;
    }

    public String toString() {
        return "Void";
    }
}