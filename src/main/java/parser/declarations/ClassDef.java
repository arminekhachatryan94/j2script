package j2script.declarations;

import j2script.statements.Statement;
import j2script.types.TypeVariable;
import j2script.declarations.VarDec;
import j2script.names.ClassName;
import java.util.Arrays;
import java.util.List;

public class ClassDef {
    public final ClassName name;
    public final ClassName extendedClass;
    public final Constructor constructor;
    public final List<VarDec> instanceVars;
    public final List<MethodDef> methodDefs;
    public final TypeVariable[] typeVariables;

    // extended class
    public ClassDef(final ClassName name,
                    final Constructor constructor,
                    final List<VarDec> instanceVars,
                    final List<MethodDef> methodDefs,
                    final TypeVariable[] typeVariables) {
      this.name = name;
      this.extendedClass = null;
      this.constructor = constructor;
      this.instanceVars = instanceVars;
      this.methodDefs = methodDefs;
      this.typeVariables = typeVariables;
    }

    //not extended
    public ClassDef(final ClassName name,
                    final Constructor constructor,
                    final ClassName extendedClass,
                    final List<VarDec> instanceVars,
                    final List<MethodDef> methodDefs,
                    final TypeVariable[] typeVariables) {
        this.name = name;
        this.constructor = constructor;
        this.extendedClass = extendedClass;
        this.instanceVars = instanceVars;
        this.methodDefs = methodDefs;
        this.typeVariables = typeVariables;
    }

    public int hashCode() {
        return name.hashCode(); 
    }

    public boolean equals(final Object other) {
        if (other instanceof ClassDef) {
            final ClassDef otherDef = (ClassDef) other;
            return (otherDef.name.equals(name) &&
                    otherDef.typeVariables.equals(typeVariables) &&
                    otherDef.extendedClass.equals(extendedClass));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("class " + name.toString());
    }
}

