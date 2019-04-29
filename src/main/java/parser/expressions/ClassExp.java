package j2script.expressions;

import j2script.names.ClassName;

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
}