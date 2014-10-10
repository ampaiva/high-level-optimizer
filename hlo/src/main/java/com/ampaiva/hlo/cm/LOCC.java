package com.ampaiva.hlo.cm;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.stmt.TryStmt;

import java.util.List;

public class LOCC extends ConcernMetric {
    public LOCC(CompilationUnit cu) {
        super(cu);
    }

    public void countMethodDeclaration(MethodDeclaration obj) {
        countThrowsinNameExpr(obj.getThrows());
        countObject(obj.getBody());
    }

    public void countConstructorDeclaration(ConstructorDeclaration obj) {
        countThrowsinNameExpr(obj.getThrows());
        countObject(obj.getBlock());
    }

    public void countThrowsinNameExpr(List<NameExpr> throws_) {
        if (throws_ == null) {
            return;
        }
        getNodes().add(
                new ConcernMetricNode(throws_.get(0).getBeginLine(), throws_.get(0).getBeginColumn(), throws_.get(
                        throws_.size() - 1).getBeginLine(), throws_.get(throws_.size() - 1).getBeginColumn()));
    }

    public void countThrowStmt(ThrowStmt stmt) {
        getNodes().add(new ConcernMetricNode(stmt));
    }

    public void countTryStmt(TryStmt tryStmt) {
        getNodes().add(tryStmt.getBeginLine(), tryStmt.getBeginColumn(), tryStmt.getTryBlock().getBeginLine(),
                tryStmt.getTryBlock().getBeginColumn());
        getNodes().add(tryStmt.getTryBlock().getEndLine(), tryStmt.getTryBlock().getEndColumn(), tryStmt.getEndLine(),
                tryStmt.getEndColumn());

        countObject(tryStmt.getTryBlock());
    }

    @Override
    public String toString() {
        return "LOCC";
    }

}
