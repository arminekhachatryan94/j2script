package j2script.tokens;

public class SingleQuoteToken implements Token {
    public int hashCode() {
        return 10000;
    }

    public boolean equals(Object obj) {
        return obj instanceof SingleQuoteToken;
    }

    public String toString() {
        return "Boolean";
    }
}