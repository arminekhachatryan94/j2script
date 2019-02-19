package j2script.tokens;
// Interface for Token classes
// Not actually needed but will allow right click > generate to generate methods in Intellij
public interface Token {
    public int hashCode();
    public boolean equals(Object other);
    public String toString();
}
