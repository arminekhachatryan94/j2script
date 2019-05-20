package j2script.expressions;
import j2script.statements.*;
public interface Exp extends Statement{
  // not needed; used to indicate that these should be overridden
  public int hashCode();
  public boolean equals(Object other);
  public String toString();
  public String emit();
}
