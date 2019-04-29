package j2script.declarations;


import j2script.statements.Statement;

import java.util.*;

public class Program {
    public final List<ClassDef> classDefs;
    public final Statement statement;

    public Program(final List<ClassDef> classDefs,
                   final Statement statement) {
        this.classDefs = classDefs;
        this.statement = statement;
    }
    public int hashCode() {
        return (statement.hashCode());
    }

    public boolean equals(final Object other) {
        if (other instanceof Program) {
            final Program otherProgram = (Program)other;
            return otherProgram.statement.equals(statement);
        } else {
            return false;
        }
    }

    public String toString() {
        return (String.join("\n", classDefs.toString()) + "\n\n" + statement.toString());
    }
}
