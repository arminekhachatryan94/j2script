package j2script.types;

import j2script.names.Name;

public class TypeVariable implements Type {
    public String name;

    public TypeVariable(String name) {
        this.name = name;
    }

    public boolean equals(Object obj) {
        if(obj instanceof TypeVariable) {
            TypeVariable typeVarObj = (TypeVariable)obj;
            return typeVarObj.name.equals(name);
        }
        return false;
    }

    public String toString() {
        return name;
    }

    public int hashCode() {
        return name.hashCode();
    }
}
