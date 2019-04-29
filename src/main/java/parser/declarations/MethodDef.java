package j2script.declarations;

import j2script.types.Type;
import j2script.types.ReturnType;
import j2script.names.MethodName;
import j2script.names.Variable;
import j2script.statements.Statement;
import j2script.access.*;

import java.util.ArrayList;
import java.util.List;

public class MethodDef {
    public final Access access;
    public final ReturnType returnType;
    public final MethodName name;
    public final List<VarDec> varDecs;
    public final Statement statement;

    public MethodDef(final Access access, final ReturnType returnType, final MethodName name, 
                     final List<VarDec> varDecs, final Statement statement) {
      this.access = access;
      this.returnType = returnType;
      this.name = name;
      this.varDecs = varDecs;
      this.statement = statement;
    }

    public int hashCode() {
        return returnType.hashCode() + name.hashCode() + 
               statement.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof MethodDef) {
            final MethodDef otherMethodDef = (MethodDef) other;
            return otherMethodDef.access.equals(access) &&
                   otherMethodDef.returnType.equals(returnType) &&
                   otherMethodDef.name.equals(name) &&
                   otherMethodDef.statement.equals(statement);
        } else {
            return false;
        }
    }

    public String toString() {
        return name.toString() + "(" + String.join(",", varDecs.toString()) + ")";
    }
}