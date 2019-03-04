package j2script;

import java.util.Arrays;

public class ClassDef {
    public final String name;
    public final ClassName extendedClass;
    public final InstanceDec[] instanceVars;
    public final Statement statement;
    public final MethodDef[] methodDefs;

    // non extended class
    public ClassDef(final String name,
                    final VariableDeclaration[] instanceVars,
                    final Statement statement,
                    final MethodDef[] methodDefs) {
        this.name = name;
        this.instanceVars = instanceVars;
        this.statement = statement;
        this.methodDefs = methodDefs;
    }

    // extended class
    public ClassDef(final String name,
                    final ClassName extendedClass,
                    final VariableDeclaration[] instanceVars,
                    final Statement statement,
                    final MethodDef[] methodDefs) {
        this.name = name;
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

