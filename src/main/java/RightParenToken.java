package j2script;
public class RightParenToken implements Token {
    public int hashCode() {
        return 19;
    }

    public boolean equals(Object obj) {
        return obj instanceof RightParenToken;
    }

    public String toString() {
        return ")";
    }
}
