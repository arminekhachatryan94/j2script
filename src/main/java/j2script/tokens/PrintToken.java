package j2script.tokens;

public class PrintToken implements Token {
    public int hashCode() {
        return 14;
    }

    public boolean equals(Object obj) {
        return obj instanceof NewToken;
    }

    public String toString() {
        return "println";
    }
}
