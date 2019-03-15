package j2script.declarations;

import j2script.statements.Statement;
import j2script.names.ClassName;
import java.util.Arrays;

public class ClassDef {
    public final ClassName name;
    public final ClassName extendedClass;
    public final Constructor constructor;
    public final InstanceDec[] instanceVars;
    public final Statement statement;
    public final MethodDef[] methodDefs;

    // non extended class
    public ClassDef(final ClassName name,
                    final Constructor constructor,
                    final InstanceDec[] instanceVars,
                    final Statement statement,
                    final MethodDef[] methodDefs) {
        this.name = name;
        this.constructor = constructor;
        this.extendedClass = null;
        this.instanceVars = instanceVars;
        this.statement = statement;
        this.methodDefs = methodDefs;
    }

    // extended class
    public ClassDef(final ClassName name,
                    final Constructor constructor,
                    final ClassName extendedClass,
                    final InstanceDec[] instanceVars,
                    final Statement statement,
                    final MethodDef[] methodDefs) {
        this.name = name;
        this.constructor = constructor;
        this.extendedClass = extendedClass;
        this.instanceVars = instanceVars;
        this.statement = statement;
        this.methodDefs = methodDefs;
    }

    public int hashCode() {
        return name.hashCode() + Arrays.deepHashCode(instanceVars) + 
        statement.hashCode() + Arrays.deepHashCode(methodDefs);
    }

    public boolean equals(final Object other) {
        if (other instanceof ClassDef) {
            final ClassDef otherDef = (ClassDef) other;
            return (otherDef.name.equals(name) &&
                    otherDef.extendedClass.equals(extendedClass) &&
                    Arrays.deepEquals(otherDef.instanceVars, instanceVars) &&
                    otherDef.statement.equals(statement) &&
                    Arrays.deepEquals(otherDef.methodDefs, methodDefs));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("class " + name.toString());
    }
}

