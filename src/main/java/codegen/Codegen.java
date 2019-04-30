package j2script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import j2script.expressions.*;
import j2script.statements.*;
import j2script.names.*;
import j2script.VTableClassTable;
import j2script.declarations.*;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.lang.instrument.ClassDefinition;


public class Codegen{
    //Code generated will be inserted into the list
    private final List<String> Code;
    private Map<ClassName, ClassDef> classes;
    //The actual vtables per class
    private List<VTableClassTable> listofVtables;
    private Map<ClassName, VTableClassTable> compmap;
    private HashMap<MethodName, Integer> offsets;
    // private final Map<ClassName, List<MethodName>> vtableMethodOffsets;
    public Codegen(){
        Code = new ArrayList<String>();
        classes = new HashMap<ClassName,ClassDef>();
        listofVtables = new ArrayList<>();
        compmap = new HashMap<ClassName, VTableClassTable>();
        offsets = new HashMap<MethodName, Integer>();
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
        else if (exp instanceof BoolExp){
            compileBoolExp((BoolExp)exp);
        }
    }
    public void compileProgram(Program prog){
        for (ClassDef cls : prog.classDefs) {
            classes.put(cls.name,cls);
        }
        for (ClassDef cls : prog.classDefs) {
            compileClass(cls);
        }
        
    }
    public void compileParentclass(ClassDef cls){
        //Vtable holds the methods with updated names
        List<MethodDef> Vtable = new ArrayList<>();
        //Connects old methodnames to new methodnames
        HashMap<MethodName, MethodName> tempname = new HashMap<MethodName,MethodName>();
        //ADD all the methods into the vtable as CNAME_MethodNAME, link old names to new names.
        //the original method name is connected to the new method (name) here
        int count =0;
        for (MethodDef md : cls.methodDefs) {
            MethodName vname = new MethodName(cls.name + "_" + md.name);
            MethodDef temp = new MethodDef(md.access, md.returnType,vname,md.varDecs,md.statement);
            Vtable.add(temp);
            tempname.put(md.name, vname);
            offsets.put(md.name, count );
            count++;
        }
        //connect vtable to class
        VTableClassTable v = new VTableClassTable(Vtable, cls, tempname, offsets);
        compmap.put(cls.name, v);
    }
    public void compileChildClass(ClassDef cls){

    }
    public void compileClass(ClassDef cls){
        if (cls.extendedClass == null && compmap.get(cls.name) == null){
            compileParentclass(cls);
            List<MethodDef> methods = compmap.get(cls.name).vTable;
            String vt = "var " + cls.name + "_Vtable" + "= [";
            for (int i =0; i < methods.size(); i++){
                vt += methods.get(i).name.toString();
                if (i+1 < methods.size()){
                    vt+=",";
                }
            }
            vt += "];";
            Code.add(vt);
        }
        else{
            //FIND PARENT AND COMPILE IT IF IT HASNT BEEN SO
            if (compmap.get(cls.extendedClass) == null){
                compileClass(classes.get(cls.extendedClass));
            }
            //COMPILE THE CLASS
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
        Code.add(e.emit());
    }
    public void compileWhileStmt(Statement whilestmt){
        WhileStatement e = (WhileStatement)whilestmt;
        Code.add(e.emit());
    }
    public void compileBinOpExp(Exp exp){
        BinopExp e = (BinopExp)exp;
        Code.add(e.emit());
    }

    public void compileBoolExp(Exp exp){
        BoolExp e = (BoolExp)exp;
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
    public void writeStatementstoFile(final Statement stmt, final File file) throws IOException{
        final Codegen gen = new Codegen();
        gen.compileStatement(stmt);
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