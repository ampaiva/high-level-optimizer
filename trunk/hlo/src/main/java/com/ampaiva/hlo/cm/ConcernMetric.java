package com.ampaiva.hlo.cm;

import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.GenericVisitorAdapter;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.ampaiva.hlo.util.Helper;

public abstract class ConcernMetric {
    private final ConcernMetricNodes nodes = new ConcernMetricNodes();
    private final String source;
    private final String key;

    public ConcernMetric(String key, InputStream in) {
        try {
            this.key = key;
            this.source = changeUnsupportedJavaFeatures(Helper.convertInputStream2String(in));
            parseSource();
        } catch (Exception e) {
            throw new IllegalArgumentException("Parser error of " + key + ":", e);
        }
    }

    private static String changeUnsupportedJavaFeatures(String source) {
        return source.replace("<>", "");
    }

    // TODO: getKey should be outside this class
    private void parseSource() throws ParseException {
        InputStream in = Helper.convertString2InputStream(source);
        CompilationUnit cu = Helper.parserClass(in);
        final GenericVisitorAdapter<ClassOrInterfaceDeclaration, StringBuilder> mva = new GenericVisitorAdapter<ClassOrInterfaceDeclaration, StringBuilder>() {
            @Override
            public ClassOrInterfaceDeclaration visit(ClassOrInterfaceDeclaration classOrInterface, StringBuilder sbKey) {
                if (sbKey.length() > 0) {
                    sbKey.append(".");
                }
                sbKey.append(classOrInterface.getName());
                // TODO: this class should start from this interface
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

    public String getSource() {
        return source;
    }

    public String getKey() {
        return key;
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

}
