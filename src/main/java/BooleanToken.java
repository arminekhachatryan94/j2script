package j2script;
public class BooleanToken implements Token {
    public int hashCode() {
        return 2;
    }

    public boolean equals(Object obj) {
        return obj instanceof BooleanToken;
    }

    public String toString() {
        return "Boolean";
    }
}
