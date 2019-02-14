package j2script;
public class SubtractToken implements Token {
    public int hashCode() {
        return 21;
    }

    public boolean equals(Object obj) {
        return obj instanceof SubtractToken;
    }

    public String toString() {
        return "-";
    }
}
