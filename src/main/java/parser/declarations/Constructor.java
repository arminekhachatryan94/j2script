package j2script;

import java.util.Arrays;

public class Constructor {
    public final VarDec[] parameters;
    public final Statement statement;

    public Constructor(final VarDec[] parameters,
                       final Statement statement) {
        this.parameters = parameters;
        this.statement = statement;
    }

    public int hashCode() {
        return statement.hashCode() + Arrays.deepHashCode(parameters);
    }

    public boolean equals(final Object other) {
        if (other instanceof Constructor) {
            final Constructor otherDec =
                (Constructor)other;
            return (otherDec.statement.equals(statement) &&
                    Arrays.deepEquals(otherDec.parameters, parameters));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("constructor(" + Join.join(", ", parameters) + ") { " +
                statement.toString() + " }");
    } 
}