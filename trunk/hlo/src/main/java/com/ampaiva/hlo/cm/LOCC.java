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

    public LOCC(CompilationUnit cu) {
        this.cu = cu;
    }

    public int getMetric() {
        final GenericVisitorAdapter<ClassOrInterfaceDeclaration, int[]> mva = new GenericVisitorAdapter<ClassOrInterfaceDeclaration, int[]>() {
            @Override
            public ClassOrInterfaceDeclaration visit(ClassOrInterfaceDeclaration classOrInterface, int[] total) {
                total[0] += LOCC.this.countObject(classOrInterface.getMembers());
                return null;
            }
        };
        final int[] total = new int[1];
        mva.visit(cu, total);
        return total[0];
    }

    private int countObject(Object obj) {
        int total = 0;
        if (obj != null) {
            try {
                if (obj instanceof List) {
                    total += countListObject((List<?>) obj);
                } else {
                    total += invokeCountMethod(obj);
                }
            } catch (NoSuchMethodException e) {
                total += handleNoCountMethodforType(obj, e);
            } catch (Exception e) {
                throw new IllegalArgumentException(obj.toString() + ": " + e.toString(), e);
            }
        }
        return total;
    }

    private int countListObject(List<?> list) {
        int total = 0;
        if (list != null) {
            for (Object object : list) {
                total += countObject(object);
            }
        }
        return total;
    }

    private int handleNoCountMethodforType(Object obj, NoSuchMethodException e) {
        int total = 0;
        List<Method> methods = Arrays.asList(obj.getClass().getDeclaredMethods());
        for (Method method : methods) {
            total += getStatementsInvokingMethod(new Class[] { Statement.class, List.class }, obj, method);
        }
        return total;
    }

    private int getStatementsInvokingMethod(Class<?>[] classes, Object obj, Method method) {
        int total = 0;
        for (Class<?> clazz : classes) {
            total += getStatementsInvokingMethod(clazz, obj, method);
        }
        return total;
    }

    private int getStatementsInvokingMethod(Class<?> clazz, Object obj, Method method) {
        int total = 0;
        if (clazz.isAssignableFrom(method.getReturnType()) && method.getParameterCount() == 0) {
            try {
                total += countObject(method.invoke(obj));
            } catch (Exception e1) {
                throw new IllegalArgumentException(obj.toString() + ": " + method.getName(), e1);
            }
        }
        return total;
    }

    private int invokeCountMethod(Object obj) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        int total = 0;
        Method m = this.getClass().getDeclaredMethod("count" + obj.getClass().getSimpleName(), obj.getClass());
        total += (Integer) m.invoke(this, obj);
        return total;
    }

    private int countMethodDeclaration(MethodDeclaration obj) {
        int total = 0;
        total += countThrowsinNameExpr(obj.getThrows());
        total += countObject(obj.getBody());
        return total;
    }

    private int countConstructorDeclaration(ConstructorDeclaration obj) {
        int total = 0;
        total += countThrowsinNameExpr(obj.getThrows());
        total += countObject(obj.getBlock());
        return total;
    }

    private int countThrowsinNameExpr(List<NameExpr> throws_) {
        int total = 0;
        if (throws_ == null) {
            return 0;
        }
        total += 1 + throws_.get(throws_.size() - 1).getBeginLine() - throws_.get(0).getBeginLine();
        return total;
    }

    private int countThrowStmt(ThrowStmt stmt) {
        return 1 + stmt.getEndLine() - stmt.getBeginLine();
    }

    private int countTryStmt(TryStmt tryStmt) {
        int count = 0;
        count += 1 + tryStmt.getEndLine() - tryStmt.getBeginLine();
        // Block of code inside try block excluding } (end line) and { (begin line)
        int delta = (tryStmt.getTryBlock().getEndLine() - 1) - (tryStmt.getTryBlock().getBeginLine());
        if (delta > 0) {
            count -= delta;
        }
        count += countObject(tryStmt.getTryBlock());
        return count;
    }

}
