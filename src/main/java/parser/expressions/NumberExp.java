package j2script.expressions;

public class NumberExp implements Exp {
    public final int number;

    public NumberExp(final int number) {
        this.number = number;
    }
    
    public String emit() {
        return number;
    }
}