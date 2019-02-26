package j2script.types;

public class VoidType implements Type {
    public int hashCode() {
        return 4;
    }

    public boolean equals(Object obj) {
        return obj instanceof VoidType;
    }

    public String toString() {
        return "VoidType";
    }
}