package j2script.tokens;
public class ElseToken implements Token {
    public int hashCode() {
        return 9;
    }

    public boolean equals(Object obj) {
        return obj instanceof ElseToken;
    }

    public String toString() {
        return "else";
    }
}
