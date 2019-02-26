package j2script.expressions;

import j2script.types.Type;
import j2script.access.Access;

import java.util.ArrayList;

public class MethodDefExp implements Exp {
    public final Access access;
    public final Type type;
    public final String name;
    public final ArrayList<VarDecExp> varDecs;
    public final Exp statement;

    public MethodDefExp(final Access access, final Type type, final String name, final ArrayList<VarDecExp> varDecs, final Exp statement) {
      this.access = access;
      this.type = type;
      this.name = name;
      this.varDecs = varDecs;
      this.statement = statement;
    }
}