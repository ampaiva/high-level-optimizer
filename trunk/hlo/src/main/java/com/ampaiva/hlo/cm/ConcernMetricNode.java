package com.ampaiva.hlo.cm;

import japa.parser.ast.Node;

public final class ConcernMetricNode implements Comparable<ConcernMetricNode> {

    private final int beginLine;
    private final int beginColumn;
    private final int endLine;
    private final int endColumn;
    private int offset;
    private int length;

    public ConcernMetricNode(String source, int beginLine, int beginColumn, int endLine, int endColumn) {
        this.beginLine = beginLine;
        this.beginColumn = beginColumn;
        this.endLine = endLine;
        this.endColumn = endColumn;
        getCodePosition(source);
    }

    public ConcernMetricNode(String source, Node node) {
        this(source, node.getBeginLine(), node.getBeginColumn(), node.getEndLine(), node.getEndColumn());
    }

    private void getCodePosition(String source) {
        String[] lines = source.split("(?<=\\r?\\n)");
        int offset = 0, length = 0;
        if (lines.length < endLine) {
            lines = source.split("(?<=\\r)");
            if (lines.length < endLine) {
                throw new IllegalArgumentException("Source '" + source + "' does not contain " + this + ": "
                        + lines.length + " < " + endLine);
            }
        }
        for (int i = 0; i < lines.length; i++) {
            if (i <= beginLine - 1) {
                if (i == beginLine - 1) {
                    offset += beginColumn;
                    break;
                }
                offset += lines[i].length();
            }
        }
        for (int i = 0; i < lines.length; i++) {
            if (i <= endLine - 1) {
                if (i == endLine - 1) {
                    length += endColumn;
                    break;
                }
                length += lines[i].length();
            }
        }
        this.offset = offset;
        this.length = length - offset + 1;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public int getBeginLine() {
        return beginLine;
    }

    public int getBeginColumn() {
        return beginColumn;
    }

    public int getEndLine() {
        return endLine;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public int compareTo(ConcernMetricNode other) {
        int compare = beginLine - other.beginLine;
        if (compare == 0) {
            return endLine - other.endLine;
        }
        return compare;
    }

    @Override
    public String toString() {
        return "ConcernMetricNode [beginLine=" + beginLine + ", beginColumn=" + beginColumn + ", endLine=" + endLine
                + ", endColumn=" + endColumn + ", offset=" + offset + ", length=" + length + "]";
    }

}
