package j2script.names;

public class ClassName extends Name {
    public ClassName(final String name) {
        super(name);
    }

    public boolean sameClass(final Name other) {
        return other instanceof ClassName;
    }
}