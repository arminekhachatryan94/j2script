package j2script.tokens;
public class ClassToken implements Token {
    public int hashCode() {
        return 4;
    }

    public boolean equals(Object obj) {
        return obj instanceof ClassToken;
    }

    public String toString() {
        return "class";
    }
}
