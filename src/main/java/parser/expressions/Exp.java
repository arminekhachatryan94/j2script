package j2script.expressions;

public interface Exp {
  // not needed; used to indicate that these should be overridden
  public int hashCode();
  public boolean equals(Object other);
  public String toString();
  public String emit();
}
