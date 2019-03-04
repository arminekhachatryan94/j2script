package j2script.statements;

public interface Statement {
    // not needed; used to indicate that these should be overridden
    public int hashCode();
    public boolean equals(Object other);
    public String toString();
}