package j2script.tokens;

public class VariableToken implements Token {
    public int hashCode() {
        return 23;
    }

    public boolean equals(Object obj) {
        return obj instanceof VariableToken;
    }

    public String toString() {
        return "Variable";
    }
}