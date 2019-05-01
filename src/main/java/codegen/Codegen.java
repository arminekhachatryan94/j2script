package j2script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import j2script.expressions.*;
import j2script.statements.*;
import j2script.types.ClassType;
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
        //We know this is the top of the ladder, print the methods out as is.
        int count = 0;
        Map<MethodName, MethodDef> methodMap = new HashMap<>();
        Map<MethodName, Integer> offsets = new HashMap<>();
        List<String> vTable = new ArrayList<>(); 
        String vtable = "var " + cls.name.toString() + "_" + "vtable = [";
        for (MethodDef md : cls.methodDefs) {
            String method = "var " + cls.name.toString() + "_" + md.name.toString() + " = function(self) {\n\t" + md.statement.toString() + "};" ;
            Code.add(method);
            methodMap.put(md.name, md);
            offsets.put(md.name, count);
            vTable.add(cls.name.toString() + "_" + md.name.toString());
            vtable += cls.name.toString() + "_" + md.name.toString();
            if (count + 1 < cls.methodDefs.size()){
                vtable += ", ";
            }
            count++;
        }
        vtable += "];";
        Code.add(vtable);        
        VTableClassTable v = new VTableClassTable(vTable,cls, methodMap, offsets);
        compmap.put(cls.name, v);
    }
    public void compileChildClass(ClassDef cls){
        Map<MethodName, MethodDef> methodMap = new HashMap<>();
        Map<MethodName, Integer> offsets = new HashMap<>();
        //Inherits parents stuff
        VTableClassTable parent = compmap.get(cls.extendedClass);
        List<String> vTable = new ArrayList<>(parent.vTable.size()+1); 

        System.out.println("This classes parent is" + cls.extendedClass);
        System.out.println("This classes parent is" + parent.theClass.name);

        System.out.println("This class is" + cls.name);
        methodMap.putAll(parent.methodMap);
        System.out.println(methodMap.toString());
        offsets.putAll(parent.offsets);
        System.out.println(parent.vTable.toString());
        System.out.println(vTable.toString());
        for (String s : parent.vTable) {
            vTable.add(s);
        }
        System.out.println(vTable.toString());
        System.out.println("This classes parents vtable is " + vTable.toString());

        int count = parent.vTable.size();

        String vtable = "var " + cls.name.toString() + "_" + "vtable = [";
        for (MethodDef md : cls.methodDefs) {
            String method = "var " + cls.name.toString() + "_" + md.name.toString() + " = function(self) {\n\t" + md.statement.toString() + "};" ;
            Code.add(method);
            //check if method is being overridden
            if (methodMap.get(md.name) != null){
                if(methodMap.get(md.name).varDecs.equals(md.varDecs)){
                    int j=0;
                    System.out.println("I am here for this class" + cls.name.toString());
                    System.out.println("I am here for this method" + md.name.toString());
                    System.out.println("BY the way the exclass is " + cls.extendedClass.name.toString());
                    String exclass = cls.extendedClass.name.toString();                  
                    //replace in childs vtable
                    methodMap.replace(md.name, md);
                    for (int i = 0 ; i < vTable.size(); i++) {
                        String s =vTable.get(i);
                        System.out.println(s + " and " + exclass + "_"+ md.name.toString());
                        if (s.equals(exclass + "_"+ md.name.toString())){
                            System.out.println("I have reached here for: " + s );
                            vTable.set(i,cls.name.toString() + "_" +md.name.toString());
                            j=i;
                        }
                    }
                    offsets.replace(md.name, j);
                }
            }
            //Its not being overridden, add it to the vtable.
            else{
                methodMap.put(md.name, md);
                offsets.put(md.name, count);
                vTable.add(cls.name.toString() + "_" + md.name.toString());
                count++;
            }

        }
        int k =0;
        for (String s : vTable) {
            vtable += s;
            if (k + 1 < vTable.size()){
                vtable += ", ";
            }
            k++;
        }
        vtable += "];";
        System.out.println("Class " + cls.name.toString() + " has this vtable " + vtable.toString());
        Code.add(vtable);    
        VTableClassTable v = new VTableClassTable(vTable,cls, methodMap, offsets);
        compmap.put(cls.name, v);
    }
    public void compileClass(ClassDef cls){
        //If doesnt extend and hasnt been compiled, compile it.
        System.out.println("Class " + cls.name.toString() + 
        "extended = " + (cls.extendedClass == null) + (compmap.get(cls.name) == null));
        if (cls.extendedClass == null && compmap.get(cls.name) == null){
            compileParentclass(cls);
        }
        else{
            //FIND PARENT AND COMPILE IT IF IT HASNT BEEN SO
            System.out.println("This is the result of compmap ");
            System.out.println("This is the result of compmap " +
            compmap.get(cls.extendedClass) == null);
            if (compmap.get(cls.extendedClass) == null){
                compileClass(classes.get(cls.extendedClass));
            }
            //Now compile the extended class itself
            compileChildClass(cls);
        }
    }
    public void compileStatement(Statement stmt){
        if (stmt instanceof IfStatement){
            compileIfStmt((IfStatement)stmt);
        }
        else if (stmt instanceof WhileStatement){
            compileWhileStmt((WhileStatement)stmt);
        }
        else if (stmt instanceof Block){

        }
        else if (stmt instanceof BreakStatement){
            
        }
        else if (stmt instanceof PrintStatement){
            
        }
        else if (stmt instanceof ReturnExpStatement){
            
        }
        else if (stmt instanceof ReturnVoidStatement){
            
        }
        else if (stmt instanceof VarAssignment){
            
        }
        else if (stmt instanceof VarDecAssignment){
            compilevarDecAssign((VarDecAssignment)stmt);
            
        }
    }
    public void compilevarDecAssign(Statement stmt){
        //Assuming this type checks, check if it is a class type and if so then create a json with the appropriate fields.
        VarDecAssignment v = (VarDecAssignment)stmt;
        if (v.varDec.type instanceof ClassType){
            ClassName cname = new ClassName(v.varDec.type.toString());
            String actualCode = "var " + v.varDec.var.toString() + " = {\n\tvtable: " + cname.toString() + "_vtable";
            VTableClassTable vt = compmap.get(cname);
            //if class has no variables, thats about it.
            if (vt.theClass.varDecs.isEmpty()){
                actualCode += "\n}";
            }
            //else check the class' constructor and instantiate the fields
            else{
                if(v.exp instanceof ClassExp){
                    ClassExp cexp = (ClassExp) v.exp;
                    actualCode +=",\n\t";
                    for(int i=0; i < vt.theClass.constructor.parameters.size(); i++){
                        actualCode += vt.theClass.varDecs.get(i).var.toString() + ": " + cexp;

                    }
                }

            }

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
    public void writeProgramtoFile(final Program program, final File file) throws IOException{
        final Codegen gen = new Codegen();
        gen.compileProgram(program);
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