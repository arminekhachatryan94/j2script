package j2script.tokens;
public class BreakToken implements Token {
    public int hashCode() {
        return 4;
    }

    public boolean equals(Object obj) {
        return obj instanceof BreakToken;
    }

    public String toString() {
        return "break";
    }
}
