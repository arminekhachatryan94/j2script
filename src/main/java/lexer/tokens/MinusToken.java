package j2script.tokens;
public class MinusToken implements Token {
    public int hashCode() {
        return 19;
    }

    public boolean equals(Object obj) {
        return obj instanceof MinusToken;
    }

    public String toString() {
        return "-";
    }
}
