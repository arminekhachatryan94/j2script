package j2script.expressions;
import j2script.names.*;

public class StringExp implements Exp {
    public final StringName name;

    public StringExp(final StringName name) {
        this.name = name;
    }

    public int hashCode() { 
        return name.hashCode(); 
    }

    public boolean equals(final Object other) {
        return (other instanceof StringExp &&
                ((StringExp)other).name.equals(name));
    }

    public String toString() { return name.toString(); }

    public String emit() {
        return "\"" + name + "\"";
    }
}