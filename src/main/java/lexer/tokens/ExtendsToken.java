package j2script.tokens;
public class ExtendsToken implements Token {
    public int hashCode() {
        return 11;
    }

    public boolean equals(Object obj) {
        return obj instanceof ExtendsToken;
    }

    public String toString() {
        return "extends";
    }
}
