package j2script.names;

public class MethodName extends Name {
    public MethodName(final String name) {
        super(name);
    }

    public boolean sameClass(final Name other) {
        return other instanceof MethodName;
    }
}