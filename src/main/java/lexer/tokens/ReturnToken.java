package j2script.tokens;
public class ReturnToken implements Token {
    public int hashCode() {
        return 17;
    }

    public boolean equals(Object obj) {
        return obj instanceof ReturnToken;
    }

    public String toString() {
        return "return";
    }
}
