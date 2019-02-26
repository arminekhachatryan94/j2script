package j2script.expressions;

public class VarEqualityExp implements Exp {
    public final VariableExp var;
    public final Exp expression;

    public VarEqualityExp(final VariableExp var,
                final Exp expression) {
        this.var = var;
        this.expression = expression;
    }
}