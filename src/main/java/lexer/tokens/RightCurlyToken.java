package j2script.tokens;
public class RightCurlyToken implements Token {
    public int hashCode() {
        return 27;
    }

    public boolean equals(Object obj) {
        return obj instanceof RightCurlyToken;
    }

    public String toString() {
        return "}";
    }
}
