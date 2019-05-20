package j2script.declarations;

import j2script.types.Type;
import j2script.names.MethodName;
import j2script.names.Variable;
import j2script.statements.Statement;
import j2script.access.*;

import java.util.Arrays;
import java.util.List;

public class MethodDef {
    public final Access access;
    public final Type returnType;
    public final MethodName name;
    public final List<VarDec> varDecs;
    public final Statement body;

    public MethodDef(final Access access, final Type returnType, final MethodName name, 
                     final List<VarDec> varDecs, final Statement body) {
      this.access = access;
      this.returnType = returnType;
      this.name = name;
      this.varDecs = varDecs;
      this.body = body;
    }

    public int hashCode() {
        return access.hashCode() + returnType.hashCode() + name.hashCode() + 
               Arrays.deepHashCode(varDecs.toArray()) + body.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof MethodDef) {
            final MethodDef otherMethodDef = (MethodDef) other;
            return otherMethodDef.access.equals(access) &&
                   otherMethodDef.returnType.equals(returnType) &&
                   otherMethodDef.name.equals(name) &&
                   Arrays.deepEquals(otherMethodDef.varDecs.toArray(), varDecs.toArray()) &&
                   otherMethodDef.body.equals(body);
        } else {
            return false;
        }
    }

    public String toString() {
        return name.toString() + "(" + String.join(",", varDecs.toString()) + ")";
    }
}