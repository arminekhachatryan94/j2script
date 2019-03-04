package j2script.names;

public abstract class Name {
    public final String name;

    public Name(final String name) {
        this.name = name;
    }

    public abstract boolean sameClass(Name other);
    
    public int hashCode() { return name.hashCode(); }
    public String toString() { return name; }
    public boolean equals(final Object other) {
        if (other instanceof Name) {
            final Name otherName = (Name)other;
            return (otherName.name.equals(name) &&
                    sameClass(otherName));
        } else {
            return false;
        }
    }
}