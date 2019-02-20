package j2script.tokens;
public class SubtractToken implements Token {
    public int hashCode() {
        return 22;
    }

    public boolean equals(Object obj) {
        return obj instanceof SubtractToken;
    }

    public String toString() {
        return "-";
    }
}
