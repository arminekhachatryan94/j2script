package j2script.types;

public interface ReturnType extends Type{
    // not needed; used to indicate that these should be overridden
    public int hashCode();
    public boolean equals(Object other);
    public String toString();
}