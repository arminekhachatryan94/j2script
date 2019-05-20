package j2script.types;

import j2script.names.StringName;

public class StringType implements Type {

    public StringType() {}

    public int hashCode() {
        return 4;
    }

    public boolean equals(Object obj) {
        return obj instanceof StringType;
    }

    public String toString() {
        return "StringType";
    }
}