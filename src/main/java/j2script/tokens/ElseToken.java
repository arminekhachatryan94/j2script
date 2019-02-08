package j2script.tokens;

public class ElseToken implements Token {
    public int hashCode() {
        return 10000;
    }

    public boolean equals(Object obj) {
        return obj instanceof ElseToken;
    }

    public String toString() {
        return "Boolean";
    }
}