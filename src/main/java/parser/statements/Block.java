package j2script.statements;

public class Block implements Statement {
    public List<Statement> statements;
    public Block(List<Statement> statements){
        this.statements = statements;
    }
    public int hashCode() {
        //TODO
    }

    public boolean equals(final Object other) {
        //TODO
    }

    public String toString() {
        //TODO
    }
}