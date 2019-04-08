package j2script.names;

public class StringName extends Name {
    public StringName(final String name) {
        super(name);
    }

    public boolean sameClass(final Name other) {
        return other instanceof StringName;
    }
}