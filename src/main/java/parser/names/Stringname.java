package j2script.names;

public class Stringname extends Name {
    public Stringname(final String name) {
        super(name);
    }

    public boolean sameClass(final Name other) {
        return other instanceof Stringname;
    }
}