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

    public CodePosition getCodePosition(String source) {
        String[] lines = source.split("(?<=\\r?\\n)");
        int position = 0, offset = 0;
        if (lines.length >= getEndLine()) {
            for (int i = 0; i < lines.length; i++) {
                if (i <= getBeginLine() - 1) {
                    if (i == getBeginLine() - 1) {
                        position += getBeginColumn();
                        break;
                    }
                    position += lines[i].length();
                }
            }
            for (int i = 0; i < lines.length; i++) {
                if (i <= getEndLine() - 1) {
                    if (i == getEndLine() - 1) {
                        offset += getEndColumn();
                        break;
                    }
                    offset += lines[i].length();
                }
            }
            return new CodePosition(position, offset - position);
        }

        throw new IllegalArgumentException("Source '" + source + "' does not contain " + this);
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
