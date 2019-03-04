package j2script.expressions;

import j2script.types.Type;
import j2script.access.Access;

public class MethodDef {
    public final Access access;
    public final ReturnType returnType;
    public final String name;
    public final VarDecExp[] varDecs;
    public final Statement statement;

    public MethodDef(final Access access, final ReturnType returnType, final String name, 
                     final VarDecExp[] varDecs, final Statement statement) {
      this.access = access;
      this.returnType = returnType;
      this.name = name;
      this.varDecs = varDecs;
      this.statement = statement;
    }

    public int hashCode() {
        return access.hashCode() + returnType.hashCode() + name.hashcode() + 
               Array.deepHashCode(varDecs) + statement.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof MethodDef) {
            final MethodDef otherMethodDef = (MethodDef) other;
            return otherMethodDef.access.equals(access) &&
                   otherMethodDef.returnType.equals(returnType) &&
                   otherMethodDef.name.equals(name) &&
                   Arrays.deepEquals(otherMethodDef.varDecs, varDecs) &&
                   otherMethodDef.statement.equals(statement);
        } else {
            return false;
        }
    }

    public String toString() {
        return name.toString() + "(" + Join.join(",", varDecs.type) + ")";
    }
}