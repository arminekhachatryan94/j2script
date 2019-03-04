package j2script.declarations;

import j2script.declarations.VarDec;
import j2script.statements.Statement;

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
        return ("constructor(" + String.join(", ", parameters.toString()) + ") { " +
                statement.toString() + " }");
    } 
}