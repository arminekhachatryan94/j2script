package j2script.expressions;

public class NumberExp implements Exp {
    public final int number;

    public NumberExp(final int number) {
        this.number = number;
    }

    public boolean equals(final Object other) {
        final NumberExp otherExp = (NumberExp) other;
        return otherExp.number == number;
    }

    public String toString() {
        return number + "";
    }
    
    public String emit() {
        return number + "";
    }
}