package j2script.names;

public class Methodname extends Name {
    public Methodname(final String name) {
        super(name);
    }

    public boolean sameClass(final Name other) {
        return other instanceof Methodname;
    }
}