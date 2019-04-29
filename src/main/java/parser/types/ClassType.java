package j2script.types;

public class ClassType implements Type, ReturnType {
    public final String name;

    public ClassType(String name) {
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
        return "ClassType";
    }
}