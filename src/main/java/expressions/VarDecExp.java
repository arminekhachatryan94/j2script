package j2script.expressions;

public class VarDecExp implements Exp {
    public final Type type;
    public final String name;

    public VarDecExp(final Type type, final String name) {
        this.type = type;
        this.name = name;
    }
}