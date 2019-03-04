package j2script.tokens;
public class ConstructorToken implements Token {
    public int hashCode() {
        return 31;
    }

    public boolean equals(Object obj) {
        return obj instanceof ConstructorToken;
    }

    public String toString() {
        return "constructor";
    }
}
