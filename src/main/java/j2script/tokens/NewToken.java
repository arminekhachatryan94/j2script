package j2script.tokens;

public class NewToken implements Token {
    public int hashCode() {
        return 13;
    }

    public boolean equals(Object obj) {
        return obj instanceof NewToken;
    }

    public String toString() {
        return "new";
    }
}