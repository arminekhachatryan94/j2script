package j2script;
public class BreakToken implements Token {
    public int hashCode() {
        return 3;
    }

    public boolean equals(Object obj) {
        return obj instanceof BreakToken;
    }

    public String toString() {
        return "break";
    }
}
