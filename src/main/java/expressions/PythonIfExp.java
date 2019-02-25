package j2script.expressions;

public class PythonIfExp implements Exp {
    public final Exp guard;
    public final Exp[] elifs;
    public final Exp elseThis;
}