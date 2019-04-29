package j2script.declarations;

import j2script.statements.Statement;
import j2script.declarations.VarDec;
import j2script.names.ClassName;
import java.util.Arrays;
import java.util.List;

public class ClassDef {
    public final ClassName name;
    public final ClassName extendedClass;
    public final Constructor constructor;
    public final List<VarDec> varDecs;
    public final List<MethodDef> methodDefs;

    // extended class
    public ClassDef(final ClassName name,
                    final Constructor constructor,
                    final ClassName extendedClass,
                    final List<VarDec> varDecs,
                    final List<MethodDef> methodDefs) {
        this.name = name;
        this.constructor = constructor;
        this.extendedClass = extendedClass;
        this.varDecs = varDecs;
        this.methodDefs = methodDefs;
    }
    //not extended
    public ClassDef(final ClassName name,
    final Constructor constructor,
    final List<VarDec> varDecs,
    final List<MethodDef> methodDefs) {
        this.name = name;
        this.constructor = constructor;
        this.extendedClass = null;
        this.varDecs = varDecs;
        this.methodDefs = methodDefs;
    }

    public int hashCode() {
        return name.hashCode(); 
    }

    public boolean equals(final Object other) {
        if (other instanceof ClassDef) {
            final ClassDef otherDef = (ClassDef) other;
            return (otherDef.name.equals(name) &&
                    otherDef.extendedClass.equals(extendedClass));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("class " + name.toString());
    }
}

