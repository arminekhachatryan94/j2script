package j2script;
public class RightCurlyToken implements Token {
    public int hashCode() {
        return 18;
    }

    public boolean equals(Object obj) {
        return obj instanceof RightCurlyToken;
    }

    public String toString() {
        return "}";
    }
}
