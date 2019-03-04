package j2script.names;

public class Classname extends Name {
    public Classname(final String name) {
        super(name);
    }

    public boolean sameClass(final Name other) {
        return other instanceof Classname;
    }
}