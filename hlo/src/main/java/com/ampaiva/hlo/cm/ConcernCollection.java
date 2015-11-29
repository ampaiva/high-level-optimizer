package com.ampaiva.hlo.cm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.type.Type;

public class ConcernCollection extends ConcernMetric implements IMethodCalls {

    private static final String DOT = ".";
    private final List<String> methodSources = new ArrayList<String>();
    private final List<String> methodNames = new ArrayList<String>();
    private final List<List<String>> sequences = new ArrayList<List<String>>();
    private final Map<String, String> variables = new HashMap<String, String>();

    private void addSequence(String methodName, String methodSource) {
        StringBuilder sb = new StringBuilder();
        if (cu.getPackage() != null) {
            sb.append(cu.getPackage().getName()).append(DOT);
        }
        sb.append(cu.getTypes().get(0).getName()).append(DOT);
        sb.append(methodName);
        methodNames.add(sb.toString());
        methodSources.add(methodSource);
        sequences.add(new ArrayList<String>());
    }

    public void countClassOrInterfaceDeclaration(ClassOrInterfaceDeclaration obj) {
        addSequence("", obj.toString());
    }

    public void countFieldDeclaration(FieldDeclaration obj) {
        for (VariableDeclarator variable : obj.getVariables()) {
            variables.put(variable.getId().getName(), obj.getType().toString());
        }
    }

    public void countConstructorDeclaration(ConstructorDeclaration obj) {
        addSequence(obj.getName(), obj.toString());
        parametersToVariables(obj.getParameters());
    }

    public void countMethodDeclaration(MethodDeclaration obj) {
        addSequence(obj.getName(), obj.toString());
        parametersToVariables(obj.getParameters());
    }

    private void parametersToVariables(List<Parameter> parameters) {
        if (parameters != null) {
            for (int i = 0; i < parameters.size(); i++) {
                Parameter parameter = parameters.get(i);
                Type parameterType = parameter.getType();
                variables.put(parameter.getId().getName(), parameterType.toString());
            }
        }
    }

    public void countObjectCreationExpr(ObjectCreationExpr obj) {
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
        countObject(obj.getScope());
        getNodes().add(getSource(), obj.getBeginLine(), obj.getBeginColumn(), obj.getEndLine(), obj.getEndColumn());
        List<String> lastSequence = sequences.get(sequences.size() - 1);
        lastSequence.add(getFullName(obj.getName(), obj.getScope()));
    }

    public void countVariableDeclarationExpr(VariableDeclarationExpr obj) {
        for (VariableDeclarator variable : obj.getVars()) {
            variables.put(variable.getId().getName(), obj.getType().toString());
        }
    }

    public void countCatchClause(CatchClause obj) {
        variables.put(obj.getExcept().getId().toString(), obj.getExcept().getTypes().get(0).toString());
    }

    private String getStaticImport(Expression scope) {
        List<ImportDeclaration> imports = cu.getImports();
        if (imports == null || imports.size() == 0) {
            return null;
        }
        for (ImportDeclaration importDeclaration : imports) {
            if (importDeclaration.isStatic() && importDeclaration.getName().toString().endsWith(DOT + scope)) {
                StringBuilder fullName = new StringBuilder();
                fullName.append(importDeclaration.getName().toString().substring(0,
                        importDeclaration.getName().toString().lastIndexOf(DOT + scope) + 1));
                return fullName.toString();
            }
        }
        return null;
    }

    private String getImport(String objName) {
        List<ImportDeclaration> imports = cu.getImports();
        if (imports == null || imports.size() == 0) {
            return null;
        }
        int index1 = objName.indexOf('<');
        if (index1 > 0) {
            int index2 = objName.indexOf('>');
            if (index2 > 0 && index1 < index2) {
                objName = objName.substring(0, index1);
            }
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
        } else if (clazz != null && !clazz.contains(DOT)) {
            if (cu.getPackage() != null) {
                clazz = cu.getPackage().getName() + DOT + clazz;
            }
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
        } else if (scope != null && scope.getClass().isAssignableFrom(MethodCallExpr.class)) {
            MethodCallExpr methodCallExpr = (MethodCallExpr) scope;
            fullName.append(getFullName(methodCallExpr.getName(), methodCallExpr.getScope())).append(DOT);
        } else {

            String importStr = getStaticImport(scope);
            if (importStr == null && scope != null) {
                importStr = getImport(scope.toString());
                if (importStr != null) {
                    importStr = importStr.substring(0, importStr.length() - scope.toString().length());
                }
            }
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
    public List<String> getMethodSources() {
        return methodSources;
    }

    @Override
    public List<String> getMethodNames() {
        return methodNames;
    }

    @Override
    public List<List<String>> getSequences() {
        return sequences;
    }

    @Override
    public String toString() {
        return "ConcernCollection [methodNames=" + methodNames + "]";
    }

}
