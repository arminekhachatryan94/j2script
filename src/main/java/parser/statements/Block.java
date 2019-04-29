package j2script.statements;
import java.util.*;

public class Block implements Statement {
    public final List<Statement> statements;
    public Block(final List<Statement> statements){
        this.statements = statements;
    }
    // public int hashCode() {
    //     //TODO
    // }

    // public boolean equals(final Object other) {
    //     //TODO
    // }

    // public String toString() {
    //     //TODO
    // }
}