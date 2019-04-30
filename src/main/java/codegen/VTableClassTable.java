package j2script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import j2script.expressions.*;
import j2script.statements.*;
import j2script.names.*;
import j2script.declarations.*;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.lang.instrument.ClassDefinition;

public class VTableClassTable{
    public List<MethodDef> vTable;
    public ClassDef theClass;
    public Map<MethodName, MethodName> temp;
    public Map<MethodName, Integer> offsets;


    public VTableClassTable(List<MethodDef> vTable, ClassDef theclass,HashMap<MethodName, MethodName> temp, HashMap<MethodName,Integer> offsets){
        this.vTable=vTable;
        this.theClass=theclass;
        this.temp = temp;
        this.offsets = offsets;
    }
}