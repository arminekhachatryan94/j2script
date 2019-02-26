package j2script.expressions;

public class VarEqualityExp implements Exp {
    public final Exp var;
    public final Exp expression;

    public VarEqualityExp(final Exp var,
                final Exp expression) {
        this.var=var;
        this.expression=expression;
    }
}