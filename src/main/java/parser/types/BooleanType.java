package j2script.types;

public class BooleanType implements Type {
    public int hashCode() {
        return 1;
    }

    public boolean equals(Object obj) {
        return obj instanceof BooleanType;
    }

    public String toString() {
        return "BooleanType";
    }
}