package j2script.expressions;

public class UnaryMinusExp implements Exp {
    public final Exp child;

    public UnaryMinusExp(final Exp child) {
        this.child = child;
    }
}