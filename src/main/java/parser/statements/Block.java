package j2script.statements;
import java.util.*;

public class Block implements Statement {
    public final List<Statement> statements;

    public Block() {
        this.statements = new ArrayList<>();
    }

    public Block(final List<Statement> statements){
        this.statements = statements;
    }
    // public int hashCode() {
    //     //TODO
    // }

    public boolean equals(final Object other) {
        if (other instanceof Block) {
            final Block otherB = (Block) other;
            return otherB.statements.equals(statements);
        } else {
            return false;
        }
    }

    public String toString() {
        String statement = new String();
        for (Statement s : statements) {
            statement += s.toString();
            statement += "\n";
        }
        return statement;
    }
}