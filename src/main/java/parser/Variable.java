package j2script;

public class Variable {
    public final String name;

    public Variable(final String name) {
        this.name = name;
    }

    public 

    public boolean equals(final Object other) {
        if(other instanceof Variable) {
            Variable otherVar = (Variable) other;
            return other.name.equals(name);
        }
        else {
            return false;
        }
    }

    public String toString() {
        return ("if(" + guard.toString() + ")");
    }
}
