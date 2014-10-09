package com.ampaiva.hlo.cm;

import japa.parser.ast.Node;

import java.util.Collections;
import java.util.LinkedList;

public class ConcernMetricNodes extends LinkedList<ConcernMetricNode> {
    private static final long serialVersionUID = 6170700711734941716L;

    public int countLines() {
        int total = 0;
        int beginLine, endLine = 0;
        for (ConcernMetricNode concernMetricNode : this) {
            if (concernMetricNode.getEndLine() > endLine) {
                int delta;
                if (concernMetricNode.getBeginLine() <= endLine) {
                    delta = 0;
                    beginLine = endLine;
                } else {
                    delta = 1;
                    beginLine = concernMetricNode.getBeginLine();
                }
                beginLine = Math.max(concernMetricNode.getBeginLine(), endLine);
                endLine = concernMetricNode.getEndLine();
                total += delta + endLine - beginLine;
            }

        }
        return total;
    }

    public ConcernMetricNodes add(int beginLine, int beginColumn, int endLine, int endColumn) {
        add(new ConcernMetricNode(beginLine, beginColumn, endLine, endColumn));
        Collections.sort(this);
        return this;
    }

    public ConcernMetricNodes add(Node node) {
        return add(node.getBeginLine(), node.getBeginColumn(), node.getEndLine(), node.getEndColumn());
    }
}
