package j2script.types;

import j2script.types.ReturnType;

public class BooleanType implements Type, ReturnType {
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