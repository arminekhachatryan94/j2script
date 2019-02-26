package j2script.types;

public class StringType implements Type {
    public int hashCode() {
        return 3;
    }

    public boolean equals(Object obj) {
        return obj instanceof StringType;
    }

    public String toString() {
        return "StringType";
    }
}