package j2script;

import j2script.types.*;
import j2script.declarations.*;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class TypeRewriter {
    public final Map<TypeVariable, Type> replacements;

    public TypeRewriter(final Map<TypeVariable, Type> replacements) {
        this.replacements = replacements;
    }

    public List<Type> rewriteTypes(final List<Type> types) {
        final List<Type> result = new ArrayList(types.size());
        for (int index = 0; index < result.size(); index++) {
            result.set(index, rewriteType(types.get(index)));
        }
        return result;
    } // rewriteTypes
    
    public Type rewriteType(final Type originalType) {
        if (TypeChecker.isPrimitive(originalType)) {
            return originalType;
        } else if (originalType instanceof ClassType) {
            final ClassType asClass = (ClassType)originalType;
            final List<Type> newTypes = rewriteTypes(asClass.types);
            return new ClassType(asClass.name, newTypes);
        } else if (originalType instanceof TypeVariable) {
            final TypeVariable asVariable = (TypeVariable)originalType;
            assert(replacements.containsKey(asVariable));
            return replacements.get(asVariable);
        } else {
            assert(false);
            return null;
        }
    } // typeRewrite

    public VarDec rewriteVarDec(final VarDec vardec) {
        return new VarDec(rewriteType(vardec.type), vardec.var);
    } // rewriteVarDec

    public List<VarDec> rewriteVarDecs(final List<VarDec> vardecs) {
        final List<VarDec> result = new ArrayList<VarDec>(vardecs.size());
        for (int index = 0; index < vardecs.size(); index++) {
            result.add(rewriteVarDec(vardecs.get(index)));
        }
        return result;
    } // rewriteVarDecs

    public Constructor rewriteConstructor(final Constructor constructor) {
        return new Constructor(rewriteVarDecs(constructor.parameters),
                               constructor.body);
    } // rewriteConstructor

    public MethodDef rewriteMethodDef(final MethodDef methodDef) {
        final Map<TypeVariable, Type> newReplacements = new HashMap<TypeVariable, Type>(replacements);

        final TypeRewriter nestedRewriter = new TypeRewriter(newReplacements);
        return new MethodDef(methodDef.access,
                             nestedRewriter.rewriteType(methodDef.returnType),
                             methodDef.name,
                             nestedRewriter.rewriteVarDecs(methodDef.varDecs),
                             methodDef.body);
    } // rewriteMethodDef

    public List<MethodDef> rewriteMethodDefs(final List<MethodDef> methodDefs) {
        final List<MethodDef> result = new ArrayList<>(methodDefs.size());
        for (MethodDef m : methodDefs) {
            result.add(rewriteMethodDef(m));
        }
        return result;
    } // rewriteMethodDefs

    public Extends rewriteExtends(final Extends doesExtend) {
        if (doesExtend != null) {
            return new Extends(doesExtend.extendsName,
                               rewriteTypes(doesExtend.types));
        } else {
            return null;
        }
    } // rewriteExtends
    
    public static ClassDef rewriteClassDef(final ClassDef classDef,
                                           final List<Type> replacements) throws TypeErrorException {
        final Map<TypeVariable, Type> mapping = typeReplacementMapping(classDef.typeVariables, replacements);
        final TypeRewriter rewriter = new TypeRewriter(mapping);
        return new ClassDef(classDef.name,
                            rewriter.rewriteConstructor(classDef.constructor),
                            rewriter.rewriteExtends(classDef.extendedClass),
                            rewriter.rewriteVarDecs(classDef.instanceVars),
                            rewriter.rewriteMethodDefs(classDef.methodDefs),
                            new ArrayList());
    } // rewriteClassDefinition
    
    public static Map<TypeVariable, Type> typeReplacementMapping(final List<TypeVariable> typeVariables,
                                                                 final List<Type> replacements) throws TypeErrorException {
        if (typeVariables.size() != replacements.size()) {
            throw new TypeErrorException("Type arity mismatch; expected: " + typeVariables.size() +
                                         " types; received: " + replacements.size() + " types");
        }

        final Map<TypeVariable, Type> mapping = new HashMap<TypeVariable, Type>();
        for (int index = 0; index < typeVariables.size(); index++) {
            mapping.put(typeVariables.get(index), replacements.get(index));
        }
        return mapping;
    } // typeReplacementMapping
} // TypeRewriter
