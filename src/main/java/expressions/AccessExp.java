package j2script.expressions;

import j2script.access.Access;

public class AccessExp implements Exp {
    public final Access access;
    public final VarDecExp varDecExp;

    public AccessExp(final Access access, final VarDecExp varDecExp) {
        this.access = access;
        this.varDecExp = varDecExp;
    }
}