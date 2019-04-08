package j2script.declarations;

import j2script.statements.Statement;
import j2script.names.ClassName;
import java.util.Arrays;
import java.util.List;

public class ClassDef {
    public final ClassName name;
    public final ClassName extendedClass;
    public final Constructor constructor;
    public final List<InstanceDec> instanceVars;
    public final Statement statement;
    public final List<MethodDef> methodDefs;

    // non extended class
    public ClassDef(final ClassName name,
                    final Constructor constructor,
                    final List<InstanceDec> instanceVars,
                    final Statement statement,
                    final List<MethodDef> methodDefs) {
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
                    final List<InstanceDec> instanceVars,
                    final Statement statement,
                    final List<MethodDef> methodDefs) {
        this.name = name;
        this.constructor = constructor;
        this.extendedClass = extendedClass;
        this.instanceVars = instanceVars;
        this.statement = statement;
        this.methodDefs = methodDefs;
    }

    public int hashCode() {
        return name.hashCode() + 
        statement.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof ClassDef) {
            final ClassDef otherDef = (ClassDef) other;
            return (otherDef.name.equals(name) &&
                    otherDef.extendedClass.equals(extendedClass) &&
                    otherDef.statement.equals(statement));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("class " + name.toString());
    }
}

