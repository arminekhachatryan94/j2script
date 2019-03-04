package j2script.tokens;
public class IfToken implements Token {
    public int hashCode() {
        return 8;
    }

    public boolean equals(Object obj) {
        return obj instanceof IfToken;
    }

    public String toString() {
        return "if";
    }
}
