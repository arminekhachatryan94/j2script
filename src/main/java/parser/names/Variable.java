package j2script.names;

public class Variable extends Name {
    public Variable(final String name) {
        super(name);
    }

    public boolean sameClass(final Name other) {
        return other instanceof Variable;
    }
}