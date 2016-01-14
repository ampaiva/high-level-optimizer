package com.ampaiva.hlo.cm;

import com.ampaiva.hlo.util.SourceHandler;
import com.github.javaparser.ast.Node;

public final class ConcernMetricNode implements Comparable<ConcernMetricNode> {

    private final SourceHandler sourceHandler;

    public ConcernMetricNode(String source, int beginLine, int beginColumn, int endLine, int endColumn) {
        this.sourceHandler = new SourceHandler(source, beginLine, beginColumn, endLine, endColumn);
    }

    public ConcernMetricNode(String source, Node node) {
        this(source, node.getBeginLine(), node.getBeginColumn(), node.getEndLine(), node.getEndColumn());
    }

    public int getOffset() {
        return sourceHandler.getOffset();
    }

    public int getLength() {
        return sourceHandler.getLength();
    }

    public int getBeginLine() {
        return sourceHandler.getBeginLine();
    }

    public int getBeginColumn() {
        return sourceHandler.getBeginColumn();
    }

    public int getEndLine() {
        return sourceHandler.getEndLine();
    }

    public int getEndColumn() {
        return sourceHandler.getEndColumn();
    }

    @Override
    public int compareTo(ConcernMetricNode other) {
        int compare = getBeginLine() - other.getBeginLine();
        if (compare == 0) {
            return getEndLine() - other.getEndLine();
        }
        return compare;
    }

    public SourceHandler getSourceHandler() {
        return sourceHandler;
    }

    @Override
    public String toString() {
        return "ConcernMetricNode [sourceHandler=" + sourceHandler + "]";
    }
}
