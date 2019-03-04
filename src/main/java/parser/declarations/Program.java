package j2script;

import java.util.Arrays;

public class Program {
    public final ClassDef[] structDecs;
    public final Statement[] statements;

    public Program(final StructureDeclaration[] structDecs,
                   final Statement[] statements) {
        this.structDecs = structDecs;
        this.statements = statements;
    }

    public int hashCode() {
        return (Arrays.deepHashCode(structDecs) +
                Arrays.deepHashCode(statements));
    }

    public boolean equals(final Object other) {
        if (other instanceof Program) {
            final Program otherProgram = (Program)other;
            return (Arrays.deepEquals(otherProgram.structDecs, structDecs) &&
                    Arrays.deepEquals(otherProgram.statements, statements));
        } else {
            return false;
        }
    }

    public String toString() {
        return (Join.join("\n", structDecs) + "\n\n" +
                Join.join("\n", statements) + "\n");
    }
}
