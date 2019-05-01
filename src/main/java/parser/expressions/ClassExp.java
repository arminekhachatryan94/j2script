package j2script.expressions;

import j2script.names.*;
import j2script.declarations.*;
import j2script.statements.*;

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
        return name.hashCode();
    }

    // public boolean equals(final Object other) {
    //     if (other instanceof ClassExp) {
    //         final ClassExp otherExp = (ClassExp)other;
    //         return (otherExp.name.equals(name) &&
    //                 Arrays.deepEquals(parameters,
    //                                   otherExp.parameters));
    //     } else {
    //         return false;
    //     }
    // }
    
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
        if (cls.constructor.statement instanceof Block){
            Block s = (Block) cls.constructor.statement;
            for (Statement stmt : s.statements) {
                
            }
        }
        return json;
    }
}