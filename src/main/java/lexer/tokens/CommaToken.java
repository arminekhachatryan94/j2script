package j2script.tokens;
public class CommaToken implements Token {
    public int hashCode() {
        return 30;
    }

    public boolean equals(Object obj) {
        return obj instanceof CommaToken;
    }

    public String toString() {
        return ",";
    }
}
