package j2script.tokens;

public class IntToken implements Token {
    public int hashCode() {
        return 10000;
    }

    public boolean equals(Object obj) {
        return obj instanceof IntToken;
    }

    public String toString() {
        return "Boolean";
    }
}