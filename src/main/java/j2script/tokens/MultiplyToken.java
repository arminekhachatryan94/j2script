package j2script.tokens;

public class MultiplyToken implements Token {
    public int hashCode() {
        return 12;
    }

    public boolean equals(Object obj) {
        return obj instanceof MultiplyToken;
    }

    public String toString() {
        return "*";
    }
}
