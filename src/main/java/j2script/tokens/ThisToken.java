package j2script.tokens;

public class ThisToken implements Token {
    public int hashCode() {
        return 10000;
    }

    public boolean equals(Object obj) {
        return obj instanceof ThisToken;
    }

    public String toString() {
        return "Boolean";
    }
}