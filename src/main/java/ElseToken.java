package j2script;
public class ElseToken implements Token {
    public int hashCode() {
        return 5;
    }

    public boolean equals(Object obj) {
        return obj instanceof ElseToken;
    }

    public String toString() {
        return "else";
    }
}
