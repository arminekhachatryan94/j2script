package j2script.expressions;

import j2script.access.Access;

public class AccessExp implements Exp {
    public final Access access;
    public final Type type;
    public final String name;
    public final VarDecExp[] varDecs;
    public final Exp statement;

    public AccessExp(final Access access, final Type type, String name, VarDecExp[] varDecs, Exp statement) {
      this.access = access;
      this.type = type;
      this.name = name;
      this.varDecs = varDecs;
      this.statement = statement;
    }
}