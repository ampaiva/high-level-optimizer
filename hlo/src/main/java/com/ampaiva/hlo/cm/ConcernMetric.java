package com.ampaiva.hlo.cm;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.ampaiva.hlo.util.Helper;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;

public abstract class ConcernMetric implements IConcernMetric {
    private final ConcernMetricNodes nodes = new ConcernMetricNodes();
    protected CompilationUnit cu;
    private String source;

    private static String changeUnsupportedJavaFeatures(String source) {
        return source;
    }

    @Override
    public void parse(String source) throws ParseException {
        this.source = changeUnsupportedJavaFeatures(source);
        cu = setCU();
        doParse();
    }

    private void doParse() {
        try {
            parseSource();
        } catch (Exception e) {
            System.err.println("Parser error: " + e.toString());
        }
    }

    private void parseSource() throws ParseException {
        final GenericVisitorAdapter<ClassOrInterfaceDeclaration, StringBuilder> mva = new GenericVisitorAdapter<ClassOrInterfaceDeclaration, StringBuilder>() {
            @Override
            public ClassOrInterfaceDeclaration visit(ClassOrInterfaceDeclaration classOrInterface,
                    StringBuilder sbKey) {
                if (sbKey.length() > 0) {
                    sbKey.append(".");
                }
                sbKey.append(classOrInterface.getName());
                // TODO: this class should start from this interface
                ConcernMetric.this.countObject(classOrInterface);
                ConcernMetric.this.countObject(classOrInterface.getMembers());
                return null;
            }
        };
        nodes.clear();
        StringBuilder sbKey = new StringBuilder();
        if (cu.getPackage() != null) {
            sbKey.append(cu.getPackage().getName());
        }
        mva.visit(cu, sbKey);
    }

    private CompilationUnit setCU() throws ParseException {
        InputStream in = Helper.convertString2InputStream(source);
        return Helper.parserClass(in);
    }

    //TODO: this method should not exist. Source should be a parameter of all count methods
    public String getSource() {
        return source;
    }

    @Override
    public int getMetric() {
        return getNodes().countLines();
    }

    @Override
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
                    if (!(obj instanceof MethodCallExpr)) {
                        handleNoCountMethodforType(obj);
                    }
                }
            } catch (NoSuchMethodException e) {
                handleNoCountMethodforType(obj);
            } catch (Exception e) {
                throw new IllegalArgumentException(obj.toString() + ": " + e.toString() + " " + cu, e);
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

    private void handleNoCountMethodforType(Object obj) {
        List<Method> methods = Arrays.asList(obj.getClass().getDeclaredMethods());
        methods = sortMethods(obj, methods);
        for (Method method : methods) {
            getStatementsInvokingMethod(new Class[] { Expression.class, Statement.class, List.class }, obj, method);
        }
    }

    /*
     * This method should be overridden in case of method order
     * matter. For example: ForStmt has getInit, getCompare, getBody,
     * and getUpdate order. Analysis of getBody before getInit can
     * causes variables not available.
     */
    protected List<Method> sortMethods(Object obj, List<Method> methods) {
        return methods;
    }

    private void getStatementsInvokingMethod(Class<?>[] classes, Object obj, Method method) {
        for (Class<?> clazz : classes) {
            countStatementsInvokingMethod(clazz, obj, method);
        }
    }

    private void countStatementsInvokingMethod(Class<?> clazz, Object obj, Method method) {
        if (clazz.isAssignableFrom(method.getReturnType()) && method.getParameterTypes().length == 0) {
            try {
                countObject(method.invoke(obj));
            } catch (Exception e1) {
                throw new IllegalArgumentException(obj.toString() + ": " + method.getName(), e1);
            }
        }
    }

    private void invokeCountMethod(Object obj)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method m = this.getClass().getDeclaredMethod("count" + obj.getClass().getSimpleName(), obj.getClass());
        m.invoke(this, obj);
    }

}
