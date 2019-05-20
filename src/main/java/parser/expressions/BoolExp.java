package j2script.expressions;

public class BoolExp implements Exp {
    public final boolean value;

    public BoolExp(final boolean value) {
        this.value = value;
    }

    public int hashCode() {
        return (value) ? 1 : 0;
    }

    public boolean equals(final Object other) {
        return (other instanceof BoolExp &&
                ((BoolExp)other).value == value);
    }
    public String toString() {
        return Boolean.toString(value);
    }

    public String emit(){ 
        return value + "";
    }
}