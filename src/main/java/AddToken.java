package j2script;
public class AddToken implements Token {
    public int hashCode() {
        return 1;
    }

    public boolean equals(Object obj) {
        return obj instanceof AddToken;
    }

    public String toString() {
        return "+";
    }
}
