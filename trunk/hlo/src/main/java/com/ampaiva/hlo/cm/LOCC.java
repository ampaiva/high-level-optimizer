package com.ampaiva.hlo.cm;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.Node;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.visitor.GenericVisitorAdapter;
import japa.parser.ast.visitor.ModifierVisitorAdapter;

import java.util.List;

public class LOCC {
    private final CompilationUnit cu;

    public LOCC(CompilationUnit cu) {
        this.cu = cu;
    }

    public int getMetric() {
        final GenericVisitorAdapter<ClassOrInterfaceDeclaration, int[]> mva = new GenericVisitorAdapter<ClassOrInterfaceDeclaration, int[]>() {
            @Override
            public ClassOrInterfaceDeclaration visit(ClassOrInterfaceDeclaration cu, int[] total) {
                total[0] += LOCC.this.visit(cu);
                return null;
            }
        };
        final int[] total = new int[1];
        mva.visit(cu, total);
        return total[0];
    }

    public int getMetric_temp() {
        final int[] total = new int[1];
        final ModifierVisitorAdapter<MethodDeclaration> mva = new ModifierVisitorAdapter<MethodDeclaration>() {
            @Override
            public Node visit(MethodDeclaration m, MethodDeclaration arg) {
                total[0] = LOCC.this.visit(m);
                Node newN = super.visit(m, arg);
                return newN;
            }
        };
        mva.visit(cu, null);
        return total[0];
    }

    public int visit(ClassOrInterfaceDeclaration classOrInterface) {
        int total = 0;
        for (BodyDeclaration body : classOrInterface.getMembers()) {
            if (body instanceof InitializerDeclaration) {
                InitializerDeclaration initializer = (InitializerDeclaration) body;
                total += countTryLines(initializer.getBlock().getStmts());
            }
        }

        //        if (m.getThrows() != null && m.getThrows().size() > 0) {
        //            total += 1 + m.getThrows().get(m.getThrows().size() - 1).getBeginLine()
        //                    - m.getThrows().get(0).getBeginLine();
        //        }
        return total;
    }

    public int visit(MethodDeclaration m) {
        int total = 0;
        if (m.getThrows() != null && m.getThrows().size() > 0) {
            total += 1 + m.getThrows().get(m.getThrows().size() - 1).getBeginLine()
                    - m.getThrows().get(0).getBeginLine();
        }
        if (m.getBody() != null && m.getBody().getStmts() != null) {
            total += countTryLines(m.getBody().getStmts());
        }
        return total;
    }

    private int countTryLines(List<Statement> stmts) {
        int count = 0;
        if (stmts != null) {
            for (Statement stmt : stmts) {
                if (stmt instanceof ThrowStmt) {
                    count += 1 + stmt.getEndLine() - stmt.getBeginLine();
                } else if (stmt instanceof TryStmt) {
                    count += countTryStatement((TryStmt) stmt);
                }
            }
        }
        return count;
    }

    private int countTryStatement(TryStmt tryStmt) {
        int count = 0;
        count += 1 + tryStmt.getEndLine() - tryStmt.getBeginLine();
        // Block of code inside try block excluding } (end line) and { (begin line)
        int delta = (tryStmt.getTryBlock().getEndLine() - 1) - (tryStmt.getTryBlock().getBeginLine() + 1);
        if (delta > 0) {
            count -= delta;
        }
        count += countTryLines(tryStmt.getTryBlock().getStmts());
        return count;
    }
}
