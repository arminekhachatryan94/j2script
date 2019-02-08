package j2script.tokens;

public class ExtendsToken implements Token {
    public int hashCode() {
        return 1;
    }

    public boolean equals(Object obj) {
        return obj instanceof ExtendsToken;
    }

    public String toString() {
        return "Boolean";
    }
}
