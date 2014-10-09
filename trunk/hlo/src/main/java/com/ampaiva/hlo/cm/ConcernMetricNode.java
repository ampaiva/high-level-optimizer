package com.ampaiva.hlo.cm;

import japa.parser.ast.Node;
import japa.parser.ast.visitor.GenericVisitor;
import japa.parser.ast.visitor.VoidVisitor;

public final class ConcernMetricNode extends Node implements Comparable<ConcernMetricNode> {

    public ConcernMetricNode() {
    }

    public ConcernMetricNode(int beginLine, int beginColumn, int endLine, int endColumn) {
        super(beginLine, beginColumn, endLine, endColumn);
    }

    public ConcernMetricNode(Node node) {
        this(node.getBeginLine(), node.getBeginColumn(), node.getEndLine(), node.getEndColumn());
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return null;
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
    }

    public int compareTo(ConcernMetricNode other) {
        int compare = getBeginLine() - other.getBeginLine();
        if (compare == 0) {
            return getEndLine() - other.getEndLine();
        }
        return compare;
    }

}
