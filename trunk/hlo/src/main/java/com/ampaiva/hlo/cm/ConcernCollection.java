package com.ampaiva.hlo.cm;

import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ConcernCollection extends ConcernMetric {

    private static final String DOT = ".";
    private final List<List<String>> sequences = new ArrayList<List<String>>();

    public ConcernCollection(String key, InputStream source) {
        super(key, source);
        doParse();
    }

    public void countConstructorDeclaration(ConstructorDeclaration obj) {
        sequences.add(new ArrayList<String>());
    }

    public void countMethodCallExpr(MethodCallExpr obj) {
        getNodes().add(getSource(), obj.getBeginLine(), obj.getBeginColumn(), obj.getEndLine(), obj.getEndColumn());
        List<String> lastSequence = sequences.get(sequences.size() - 1);
        lastSequence.add(getFullName(obj));
    }

    private String getFullName(MethodCallExpr methodCall) {
        StringBuilder fullName = new StringBuilder();
        String objName = methodCall.getName();
        Expression scope = methodCall.getScope();
        if (scope == null) {
            if (cu.getPackage() != null) {
                fullName.append(cu.getPackage().getName()).append(DOT);
            }
            fullName.append(cu.getTypes().get(0).getName()).append(DOT);
        } else {
            List<ImportDeclaration> imports = cu.getImports();
            if (imports != null) {
                for (ImportDeclaration importDeclaration : imports) {
                    if (importDeclaration.getName().toString().endsWith(DOT + scope)) {
                        fullName.append(importDeclaration.getName().toString()
                                .substring(0, importDeclaration.getName().toString().lastIndexOf(DOT + scope) + 1));
                        break;
                    }
                }
            }
            fullName.append(scope).append(DOT);
        }
        fullName.append(objName);
        return fullName.toString();
    }

    @Override
    public String toString() {
        return "ConcernCollection";
    }

    public List<List<String>> getSequences() {
        return sequences;
    }
}
