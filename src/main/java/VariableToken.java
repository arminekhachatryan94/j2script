package j2script;
public class VariableToken implements Token {
    public String name;

    public VariableToken(String name) {
        this.name = name;
    }

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
