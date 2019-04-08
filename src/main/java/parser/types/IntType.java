package j2script.types;

public class IntType implements Type {
    public int hashCode() {
        return 3;
    }

    public boolean equals(Object obj) {
        return obj instanceof IntType;
    }

    public String toString() {
        return "IntType";
    }
}