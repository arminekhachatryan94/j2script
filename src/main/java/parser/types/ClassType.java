package j2script.types;

import j2script.names.ClassName;

public class ClassType implements Type {
    public final ClassName name;

    public ClassType(ClassName name) {
        this.name = name;
    }

    public int hashCode() {
        return 2;
    }

    public boolean equals(Object obj) {
        if(obj instanceof ClassType) {
            ClassType otherClassType = (ClassType) obj;
            return otherClassType.name.equals(name);
        }
        else {
            return false;
        }
    }

    public String toString() {
        return name.toString();
    }
}