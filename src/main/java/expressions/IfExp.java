package j2script.expressions;

public class IfExp implements Exp {
    public final Exp guard;
    public final Exp ifTrue;
    public final Exp ifFalse;

    public IfExp(final Exp guard,
                 final Exp ifTrue,
                 final Exp ifFalse) {
        this.guard = guard;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }
}