package j2script.types;

import j2script.names.ClassName;
import j2script.types.Type;

import java.util.List;

public class ClassType implements Type {
    public final ClassName name;
    public final List<Type> types;

    public ClassType(final ClassName name, final List<Type> types) {
        this.name = name;
        this.types = types;
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
        return "ClassType";
    }
}