package j2script.tokens;
public class VoidToken implements Token {
    public int hashCode() {
        return 33;
    }

    public boolean equals(Object obj) {
        return obj instanceof VoidToken;
    }

    public String toString() {
        return "void";
    }
}
