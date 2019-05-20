package j2script.tokens;

public class NumberToken implements Token {
    public int number;

    public NumberToken(int number) {
        this.number = number;
    }

    public String toString() {
        return Integer.toString(this.number);
    }

    public boolean equals(final Object other) {
        return (other instanceof NumberToken &&
                ((NumberToken)other).number == number);
    }

        public int hashCode() {
        return 22;
    }
}
