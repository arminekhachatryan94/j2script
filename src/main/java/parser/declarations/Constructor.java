package j2script.declarations;

import j2script.declarations.VarDec;
import j2script.statements.Statement;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

public class Constructor {
    public final List<VarDec> parameters;
    public final Statement body;

    public Constructor(final List<VarDec> parameters,
                       final Statement body) {
        this.parameters = parameters;
        this.body = body;
    }

    public int hashCode() {
        return body.hashCode() + Arrays.deepHashCode(parameters.toArray());
    }

    public boolean equals(final Object other) {
        if (other instanceof Constructor) {
            final Constructor otherDec =
                (Constructor)other;
            return (otherDec.body.equals(body) &&
                    Arrays.deepEquals(otherDec.parameters.toArray(), parameters.toArray()));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("constructor(" + String.join(", ", parameters.toString()) + ") { " +
                body.toString() + " }");
    } 
}