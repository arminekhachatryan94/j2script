package j2script.tokens;

public class SuperToken implements Token {
    public int hashCode() {
        return 30;
    }

    public boolean equals(Object obj) {
        return obj instanceof SuperToken;
    }

    public String toString() {
        return "super";
    }
}
