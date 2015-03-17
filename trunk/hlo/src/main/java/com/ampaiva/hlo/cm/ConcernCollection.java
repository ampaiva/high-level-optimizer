package com.ampaiva.hlo.cm;

import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConcernCollection extends ConcernMetric implements IMethodCalls {

    private static final String DOT = ".";
    private final List<String> methodNames = new ArrayList<String>();
    private final List<List<String>> sequences = new ArrayList<List<String>>();
    private final HashMap<String, String> variables = new HashMap<String, String>();

    public ConcernCollection(String key, InputStream source) {
        super(key, source);
        doParse();
    }

    private void addSequence(String methodName) {
        StringBuilder sb = new StringBuilder();
        if (cu.getPackage() != null) {
            sb.append(cu.getPackage().getName()).append(DOT);
        }
        sb.append(cu.getTypes().get(0).getName()).append(DOT);
        sb.append(methodName);
        methodNames.add(sb.toString());
        sequences.add(new ArrayList<String>());
    }

    public void countConstructorDeclaration(ConstructorDeclaration obj) {
        addSequence(obj.getName());
    }

    public void countMethodDeclaration(MethodDeclaration obj) {
        addSequence(obj.getName());
    }

    public void countObjectCreationExpr(ObjectCreationExpr obj) {
        getNodes().add(getSource(), obj.getBeginLine(), obj.getBeginColumn(), obj.getEndLine(), obj.getEndColumn());
        if (sequences.size() == 0) {
            addSequence("");
        }
        List<String> lastSequence = sequences.get(sequences.size() - 1);
        String importStr = getImport(obj.getType().toString());
        StringBuilder fullName = new StringBuilder();
        if (importStr != null) {
            fullName.append(importStr);
        } else {
            fullName.append(obj.getType().toString());
        }
        fullName.append(DOT);
        fullName.append(obj.getType().getName());
        lastSequence.add(fullName.toString());
    }

    public void countMethodCallExpr(MethodCallExpr obj) {
        getNodes().add(getSource(), obj.getBeginLine(), obj.getBeginColumn(), obj.getEndLine(), obj.getEndColumn());
        if (sequences.size() == 0) {
            addSequence("");
        }
        List<String> lastSequence = sequences.get(sequences.size() - 1);
        lastSequence.add(getFullName(obj.getName(), obj.getScope()));
    }

    public void countVariableDeclarationExpr(VariableDeclarationExpr obj) {
        for (VariableDeclarator variable : obj.getVars()) {
            variables.put(variable.getId().getName(), obj.getType().toString());
        }
    }

    private String getStaticImport(Expression scope) {
        List<ImportDeclaration> imports = cu.getImports();
        if (imports == null) {
            return null;
        }
        for (ImportDeclaration importDeclaration : imports) {
            if (importDeclaration.isStatic() && importDeclaration.getName().toString().endsWith(DOT + scope)) {
                StringBuilder fullName = new StringBuilder();
                fullName.append(importDeclaration.getName().toString()
                        .substring(0, importDeclaration.getName().toString().lastIndexOf(DOT + scope) + 1));
                return fullName.toString();
            }
        }
        return null;
    }

    private String getImport(String objName) {
        List<ImportDeclaration> imports = cu.getImports();
        if (imports == null) {
            return null;
        }
        for (ImportDeclaration importDeclaration : imports) {
            if (importDeclaration.getName().toString().endsWith(DOT + objName)) {
                return importDeclaration.getName().toString();
            }
        }
        return null;
    }

    private String getLocalMethodImport() {
        StringBuilder fullName = new StringBuilder();
        if (cu.getPackage() != null) {
            fullName.append(cu.getPackage().getName()).append(DOT);
        }
        fullName.append(cu.getTypes().get(0).getName());
        return fullName.toString();
    }

    private String getVariableTypeImport(String objName, Expression scope) {
        String clazz = variables.get(scope.toString());
        String importStr = getImport(clazz);
        if (importStr != null) {
            StringBuilder fullName = new StringBuilder();
            fullName.append(importStr);
            return fullName.toString();
        }
        return clazz == null ? scope.toString() : clazz;
    }

    private String getFullName(String objName, Expression scope) {
        StringBuilder fullName = new StringBuilder();
        if (scope == null && cu.getTypes().get(0).getName().equals(objName)) {
            if (cu.getPackage() != null) {
                fullName.append(cu.getPackage().getName()).append(DOT);
            }
            fullName.append(cu.getTypes().get(0).getName()).append(DOT);
        } else {
            String importStr = getStaticImport(scope);
            if (importStr == null && scope == null) {
                importStr = getLocalMethodImport();
            }
            if (importStr == null && scope != null) {
                importStr = getVariableTypeImport(objName, scope);
                scope = null;
            }
            if (importStr != null) {
                fullName.append(importStr);
            }
            if (scope != null) {
                fullName.append(scope);
            }
            fullName.append(DOT);
        }
        fullName.append(objName);
        return fullName.toString();
    }

    @Override
    public String toString() {
        return getKey();
    }

    public List<String> getMethodNames() {
        return methodNames;
    }

    public List<List<String>> getSequences() {
        return sequences;
    }
}
