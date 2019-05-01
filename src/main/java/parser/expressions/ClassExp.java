package j2script.expressions;

import j2script.expressions.ClassExp;
import j2script.declarations.ClassDef;
import j2script.names.ClassName;
import j2script.names.Variable;
import j2script.statements.Statement;
import j2script.statements.Block;

import java.util.Arrays;
import java.util.List;

public class ClassExp implements Exp {
    public final ClassName name;
    public final List<Exp> parameters;

    public ClassExp(final ClassName name,
                    final List<Exp> parameters){
        this.name=name;
        this.parameters = parameters;
    }
    public int hashCode() {
        return name.hashCode() + Arrays.deepHashCode(parameters.toArray());
    }

    public boolean equals(final Object other) {
        if (other instanceof ClassExp) {
            final ClassExp otherExp = (ClassExp)other;
            return (otherExp.name.equals(name) &&
                    Arrays.deepEquals(parameters.toArray(),
                                      otherExp.parameters.toArray()));
        } else {
            return false;
        }
    }
    
    public String toString() {
        return (name.toString() + "(" +
                String.join(", ", parameters.toString()) + ")");
    }
    public String emit() {
        String exp = name.toString() + "(";
        for(int i = 0; i < parameters.size(); i++) {
            exp += parameters.get(i).emit();
            if(i+1 < parameters.size()) {
                exp += ", ";
            }
        }
        exp += ")";
        return exp;
    }
    // Should Emit a JSON Object 
    public String emit(ClassDef cls, Variable var){
        String json = "var " + cls.name.toString() + " = {\n\tvtable: " + var.name + "_vtable,\n\t";
        //Check constructor statement
        if (cls.constructor.body instanceof Block){
            Block s = (Block) cls.constructor.body;
            for (Statement stmt : s.statements) {
                
            }
        }
        return json;
    }
}