package j2script.declarations;

import j2script.names.ClassName;
import j2script.types.Type;

import java.util.Arrays;
import java.util.List;

public class Extends {
    public final ClassName extendsName;
    public final List<Type> types;

    public Extends(final ClassName extendsName,
                   final List<Type> types) {
        this.extendsName = extendsName;
        this.types = types;
    }

    public int hashCode() {
        return extendsName.hashCode() + Arrays.deepHashCode(types.toArray());
    }

    public String toString() {
        return ("extends " + extendsName.name + "<" +
                types.toString() + ">");
    }

    public boolean equals(final Object other) {
        if (other instanceof Extends) {
            final Extends asExtends = (Extends)other;
            return (asExtends.extendsName.equals(extendsName) &&
                    Arrays.deepEquals(asExtends.types.toArray(), types.toArray()));
        } else {
            return false;
        }
    }
}
