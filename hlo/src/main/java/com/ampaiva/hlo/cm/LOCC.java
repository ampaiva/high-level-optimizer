package com.ampaiva.hlo.cm;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.visitor.GenericVisitorAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class LOCC {
    private final CompilationUnit cu;
    private final ConcernMetricNodes nodes = new ConcernMetricNodes();

    public LOCC(CompilationUnit cu) {
        this.cu = cu;
    }

    public int getMetric() {
        final GenericVisitorAdapter<ClassOrInterfaceDeclaration, int[]> mva = new GenericVisitorAdapter<ClassOrInterfaceDeclaration, int[]>() {
            @Override
            public ClassOrInterfaceDeclaration visit(ClassOrInterfaceDeclaration classOrInterface, int[] total) {
                LOCC.this.countObject(classOrInterface.getMembers());
                return null;
            }
        };
        final int[] total = new int[1];
        nodes.clear();
        mva.visit(cu, total);
        //        return total[0];
        return nodes.countLines();
    }

    private void countObject(Object obj) {
        if (obj != null) {
            try {
                if (obj instanceof List) {
                    countListObject((List<?>) obj);
                } else {
                    invokeCountMethod(obj);
                }
            } catch (NoSuchMethodException e) {
                handleNoCountMethodforType(obj, e);
            } catch (Exception e) {
                throw new IllegalArgumentException(obj.toString() + ": " + e.toString(), e);
            }
        }
    }

    private void countListObject(List<?> list) {
        if (list != null) {
            for (Object object : list) {
                countObject(object);
            }
        }
    }

    private void handleNoCountMethodforType(Object obj, NoSuchMethodException e) {
        List<Method> methods = Arrays.asList(obj.getClass().getDeclaredMethods());
        for (Method method : methods) {
            getStatementsInvokingMethod(new Class[] { Statement.class, List.class }, obj, method);
        }
    }

    private void getStatementsInvokingMethod(Class<?>[] classes, Object obj, Method method) {
        for (Class<?> clazz : classes) {
            getStatementsInvokingMethod(clazz, obj, method);
        }
    }

    private void getStatementsInvokingMethod(Class<?> clazz, Object obj, Method method) {
        if (clazz.isAssignableFrom(method.getReturnType()) && method.getParameterCount() == 0) {
            try {
                countObject(method.invoke(obj));
            } catch (Exception e1) {
                throw new IllegalArgumentException(obj.toString() + ": " + method.getName(), e1);
            }
        }
    }

    private void invokeCountMethod(Object obj) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        Method m = this.getClass().getDeclaredMethod("count" + obj.getClass().getSimpleName(), obj.getClass());
        m.invoke(this, obj);
    }

    private void countMethodDeclaration(MethodDeclaration obj) {
        countThrowsinNameExpr(obj.getThrows());
        countObject(obj.getBody());
    }

    private void countConstructorDeclaration(ConstructorDeclaration obj) {
        countThrowsinNameExpr(obj.getThrows());
        countObject(obj.getBlock());
    }

    private void countThrowsinNameExpr(List<NameExpr> throws_) {
        if (throws_ == null) {
            return;
        }
        nodes.add(new ConcernMetricNode(throws_.get(0).getBeginLine(), throws_.get(0).getBeginColumn(), throws_.get(
                throws_.size() - 1).getBeginLine(), throws_.get(throws_.size() - 1).getBeginColumn()));
    }

    private void countThrowStmt(ThrowStmt stmt) {
        nodes.add(new ConcernMetricNode(stmt));
    }

    private void countTryStmt(TryStmt tryStmt) {
        nodes.add(tryStmt.getBeginLine(), tryStmt.getBeginColumn(), tryStmt.getTryBlock().getBeginLine(), tryStmt
                .getTryBlock().getBeginColumn());
        nodes.add(tryStmt.getTryBlock().getEndLine(), tryStmt.getTryBlock().getEndColumn(), tryStmt.getEndLine(),
                tryStmt.getEndColumn());

        countObject(tryStmt.getTryBlock());
    }
}
