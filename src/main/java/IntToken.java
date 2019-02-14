package j2script;
public class IntToken implements Token {
    public int hashCode() {
        return 9;
    }

    public boolean equals(Object obj) {
        return obj instanceof IntToken;
    }

    public String toString() {
        return "int";
    }
}
