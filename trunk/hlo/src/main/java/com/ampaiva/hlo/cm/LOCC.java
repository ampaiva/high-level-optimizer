package com.ampaiva.hlo.cm;

import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.stmt.TryStmt;

import java.io.InputStream;
import java.util.List;

public class LOCC extends ConcernMetric {
    public LOCC(String key, InputStream source) {
        super(key, source);
        doParse();
    }

    public void countMethodDeclaration(MethodDeclaration obj) {
        countThrowsinNameExpr(obj.getThrows());
    }

    public void countConstructorDeclaration(ConstructorDeclaration obj) {
        countThrowsinNameExpr(obj.getThrows());
    }

    public void countThrowsinNameExpr(List<NameExpr> throws_) {
        if (throws_ == null) {
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
