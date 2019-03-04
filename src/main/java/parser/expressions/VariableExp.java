package j2script.expressions;

public class VariableExp implements Exp {
    public final String name;

    public VariableExp(final String name) {
        this.name = name;
    }
}