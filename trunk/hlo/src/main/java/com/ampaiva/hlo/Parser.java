package com.ampaiva.hlo;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.Node;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.visitor.ModifierVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Parser {
    public static CompilationUnit parserClass(File fileIn) throws ParseException {
        try {
            return JavaParser.parse(fileIn);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static private void removeFieldModifier(FieldDeclaration n, int modifier) {
        if (ModifierSet.hasModifier(n.getModifiers(), modifier)) {
            n.setModifiers(ModifierSet.removeModifier(n.getModifiers(), modifier));
        }
    }

    static private void removeMethodModifier(MethodDeclaration m, int modifier) {
        if (ModifierSet.hasModifier(m.getModifiers(), modifier)) {
            m.setModifiers(ModifierSet.removeModifier(m.getModifiers(), modifier));
        }
    }

    static public void changeToPublic(CompilationUnit cu) {
        final ModifierVisitorAdapter<FieldDeclaration> mva = new ModifierVisitorAdapter<FieldDeclaration>() {
            @Override
            public Node visit(FieldDeclaration n, FieldDeclaration arg) {
                if (!ModifierSet.isPublic(n.getModifiers())) {
                    removeFieldModifier(n, ModifierSet.PROTECTED);
                    removeFieldModifier(n, ModifierSet.PRIVATE);
                    n.setModifiers(ModifierSet.addModifier(n.getModifiers(), ModifierSet.PUBLIC));
                }
                Node newN = super.visit(n, arg);
                return newN;
            }
        };
        mva.visit(cu, null);
    }

    static public void inlineGetSet(CompilationUnit cu) {
        final ModifierVisitorAdapter<MethodDeclaration> mva = new ModifierVisitorAdapter<MethodDeclaration>() {
            @Override
            public Node visit(MethodDeclaration m, MethodDeclaration arg) {
                BlockStmt body = m.getBody();
                if (body.getStmts().size() == 1) {
                    if (!ModifierSet.isPrivate(m.getModifiers())) {
                        removeMethodModifier(m, ModifierSet.PROTECTED);
                        removeMethodModifier(m, ModifierSet.PUBLIC);
                        m.setModifiers(ModifierSet.addModifier(m.getModifiers(), ModifierSet.PRIVATE));
                    }
                }
                Node newN = super.visit(m, arg);
                return newN;
            }
        };
        mva.visit(cu, null);
    }

    public static void main(String[] args) throws ParseException, IOException {
        Map<String, CompilationUnit> cus = new HashMap<String, CompilationUnit>();
        String sourceFolder = "src";
        String pacakgeIn = "com.pdfjet";
        String packageOut = "com.pdfjet.out";
        File[] files = Helper.getFiles(sourceFolder, pacakgeIn);
        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }
            CompilationUnit cu = parserClass(file);
            List<TypeDeclaration> types = cu.getTypes();
            for (TypeDeclaration typeDeclaration : types) {
                cus.put(cu.getPackage() + "." + typeDeclaration.getName(), cu);
            }
            changeToPublic(cu);
            inlineGetSet(cu);
            NameExpr name = cu.getPackage().getName();
            name.setName(packageOut.substring(4, packageOut.length()));
            Helper.writeFile(Helper.createFile(sourceFolder, packageOut,
                    file.getName().substring(0, file.getName().length() - 5)), cu.toString());
        }
    }
}
