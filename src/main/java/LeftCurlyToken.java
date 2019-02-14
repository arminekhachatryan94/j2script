package j2script;
public class LeftCurlyToken implements Token {
    public int hashCode() {
        return 10;
    }

    public boolean equals(Object obj) {
        return obj instanceof LeftCurlyToken;
    }

    public String toString() {
        return "{";
    }
}
