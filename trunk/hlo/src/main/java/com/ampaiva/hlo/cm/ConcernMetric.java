package com.ampaiva.hlo.cm;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.GenericVisitorAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public abstract class ConcernMetric {
    private final ConcernMetricNodes nodes = new ConcernMetricNodes();
    private final CompilationUnit cu;

    public ConcernMetric(CompilationUnit cu) {
        this.cu = cu;
        final GenericVisitorAdapter<ClassOrInterfaceDeclaration, int[]> mva = new GenericVisitorAdapter<ClassOrInterfaceDeclaration, int[]>() {
            @Override
            public ClassOrInterfaceDeclaration visit(ClassOrInterfaceDeclaration classOrInterface, int[] total) {
                ConcernMetric.this.countObject(classOrInterface.getMembers());
                return null;
            }
        };
        final int[] total = new int[1];
        nodes.clear();
        mva.visit(cu, total);
    }

    public int getMetric() {
        return getNodes().countLines();
    }

    public ConcernMetricNodes getNodes() {
        return nodes;
    }

    protected void countObject(Object obj) {
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

    public String getKey() {
        final GenericVisitorAdapter<ClassOrInterfaceDeclaration, StringBuilder> mva = new GenericVisitorAdapter<ClassOrInterfaceDeclaration, StringBuilder>() {
            @Override
            public ClassOrInterfaceDeclaration visit(ClassOrInterfaceDeclaration classOrInterface, StringBuilder total) {
                if (total.length() > 0) {
                    total.append(".");
                }
                total.append(classOrInterface.getName());
                return null;
            }
        };
        StringBuilder total = new StringBuilder();
        if (cu.getPackage() != null) {
            total.append(cu.getPackage().getName());
        }
        mva.visit(cu, total);
        return total.toString();
    }

}
