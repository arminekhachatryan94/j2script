package j2script;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import j2script.expressions.Exp;
import j2script.expressions.BinopExp;
import j2script.expressions.ClassExp;
import j2script.expressions.NumberExp;
import j2script.expressions.StringExp;
import j2script.expressions.VarMethodExp;
import j2script.names.ClassName;
import j2script.names.MethodName;
import j2script.statements.IfStatement;
import j2script.statements.Statement;
import j2script.statements.WhileStatement;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.lang.instrument.ClassDefinition;

public class Codegen{
    //Code generated will be inserted into the list
    private final List<String> Code;
    private final Map<ClassName, ClassDefinition> classes;
    private final Map<ClassName, List<MethodName>> vtableMethodOffsets;
    public Codegen(){
        Code = new ArrayList<String>();
    }
    public void compileExp(Exp exp){
        if (exp instanceof BinopExp){
            compileBinOpExp((BinopExp)exp);
        }
        else if (exp instanceof NumberExp){
            compileNumberExp((NumberExp)exp);
        }
        else if (exp instanceof ClassExp){
            compileClassExp((ClassExp)exp);
        }
        else if (exp instanceof StringExp){
            compileStringExp((StringExp)exp);
        }
        else if (exp instanceof VarMethodExp){
            compileVarMethodExp((VarMethodExp)exp);
        }
    }
    public void compileStatement(Statement stmt){
        if (stmt instanceof IfStatement){
            compileIfStmt((IfStatement)stmt);
        }
        else if (stmt instanceof WhileStatement){
            compileWhileStmt((WhileStatement)stmt);
        }
    }
    public void compileIfStmt(Statement ifstmt){
        IfStatement e = (IfStatement)ifstmt;
        // Code.add(e.emit());
    }
    public void compileWhileStmt(Statement whilestmt){
        WhileStatement e = (WhileStatement)whilestmt;
        // Code.add(e.emit());
    }
    public void compileBinOpExp(Exp exp){
        BinopExp e = (BinopExp)exp;
        Code.add(e.emit());
    }
    
    public void compileClassExp(Exp exp){
        ClassExp e = (ClassExp)exp;
        Code.add(e.emit());
    }

    public void compileNumberExp(Exp exp){
        NumberExp e = (NumberExp)exp;
        Code.add(e.emit());
    }
    
    public void compileStringExp(Exp exp){
        StringExp e = (StringExp)exp;
        Code.add(e.emit());
    }

    public void compileVarMethodExp(Exp exp){
        VarMethodExp e = (VarMethodExp)exp;
        Code.add(e.emit());
    }
    public void writeExptoFile(final Exp exp, final File file) throws IOException{
        final Codegen gen = new Codegen();
        gen.compileExp(exp);
        gen.writeCompleteFile(file);
    }
    public void writeCompleteFile(final File file) throws IOException{
        final PrintWriter output= new PrintWriter(new BufferedWriter(new FileWriter(file)));
        try{
            for (final String c : Code){
                output.println(c);
            }
        }finally{
            output.close();
        }

    }
}