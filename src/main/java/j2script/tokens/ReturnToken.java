package j2script.tokens;

public class ReturnToken implements Token {
    public int hashCode() {
        return 10000;
    }

    public boolean equals(Object obj) {
        return obj instanceof ReturnToken;
    }

    public String toString() {
        return "Boolean";
    }
}