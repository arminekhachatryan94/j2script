package j2script.tokens;

public class DoubleQuoteToken implements Token {
    public int hashCode() {
        return 10000;
    }

    public boolean equals(Object obj) {
        return obj instanceof DoubleQuoteToken;
    }

    public String toString() {
        return "Boolean";
    }
}