package j2script;
public class LeftParenToken implements Token {
    public int hashCode() {
        return 11;
    }

    public boolean equals(Object obj) {
        return obj instanceof LeftParenToken;
    }

    public String toString() {
        return "(";
    }
}
