package j2script.expressions;

public class WhileExp implements Exp {
    public final Exp condition;
    public final Exp stmt;


    public WhileExp(final Exp condition,
                 final Exp stmt) {
        this.condition = condition;
        this.stmt = stmt;
    }
}