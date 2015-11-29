package com.ampaiva.hlo.cm;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;

import java.util.List;

public class LOCC extends ConcernMetric {
    public void countMethodDeclaration(MethodDeclaration obj) {
        countThrowsinNameExpr(obj.getThrows());
    }

    public void countConstructorDeclaration(ConstructorDeclaration obj) {
        countThrowsinNameExpr(obj.getThrows());
    }

    public void countThrowsinNameExpr(List<NameExpr> throws_) {
        if (throws_ == null || throws_.size()==0) {
            return;
        }

        String source = getSource();
        ConcernMetricNode concernMetricNode = new ConcernMetricNode(source, throws_.get(0).getBeginLine(), throws_.get(
                0).getBeginColumn(), throws_.get(throws_.size() - 1).getEndLine(), throws_.get(throws_.size() - 1)
                .getEndColumn());
        String sourceUntilThrows = source.substring(0, concernMetricNode.getOffset() - 1);
        int throwsColumn = concernMetricNode.getOffset() - 8;

        while (!sourceUntilThrows.substring(throwsColumn, throwsColumn + 6).equals("throws")) {
            throwsColumn--;
        }
        getNodes().add(
                new ConcernMetricNode(source, concernMetricNode.getBeginLine(), concernMetricNode.getBeginColumn()
                        - (concernMetricNode.getOffset() - (throwsColumn + 1)), concernMetricNode.getEndLine(),
                        concernMetricNode.getEndColumn()));
    }

    public void countThrowStmt(ThrowStmt stmt) {
        getNodes().add(new ConcernMetricNode(getSource(), stmt));
    }

    public void countTryStmt(TryStmt tryStmt) {
        getNodes().add(getSource(), tryStmt.getBeginLine(), tryStmt.getBeginColumn(),
                tryStmt.getTryBlock().getBeginLine(), tryStmt.getTryBlock().getBeginColumn());
        getNodes().add(getSource(), tryStmt.getTryBlock().getEndLine(), tryStmt.getTryBlock().getEndColumn(),
                tryStmt.getEndLine(), tryStmt.getEndColumn());
    }

    @Override
    public String toString() {
        return "LOCC";
    }

}
