package j2script.tokens;

public class NumberToken implements Token {
    public int number;

    public NumberToken(int number) {
        this.number = number;
    }
}