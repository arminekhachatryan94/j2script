package j2script;
public class PrivateToken implements Token {
    public int hashCode() {
        return 15;
    }

    public boolean equals(Object obj) {
        return obj instanceof PrivateToken;
    }

    public String toString() {
        return "private";
    }
}
