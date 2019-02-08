package j2script.tokens;

public class VoidToken implements Token {
    public int hashCode() {
        return 10000;
    }

    public boolean equals(Object obj) {
        return obj instanceof VoidToken;
    }

    public String toString() {
        return "Boolean";
    }
}