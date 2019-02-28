package j2script.expressions;

import j2script.access.Access;

public class InstanceDecExp implements Exp {
    public final Access access;
    public final VarDecExp vardec;

    public InstanceDecExp(final Access access, final VarDecExp vardec) {
        this.access = access;
        this.vardec = vardec;
    }
}