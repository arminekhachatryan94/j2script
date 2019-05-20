package j2script.tokens;
public class PublicToken implements Token {
    public int hashCode() {
        return 25;
    }

    public boolean equals(Object obj) {
        return obj instanceof PublicToken;
    }

    public String toString() {
        return "public";
    }
}
