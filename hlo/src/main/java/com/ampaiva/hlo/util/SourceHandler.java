package com.ampaiva.hlo.util;

import java.util.Arrays;

public class SourceHandler {
    private final int beginLine;
    private final int beginColumn;
    private int endLine;
    private int endColumn;
    private int offset;
    private int length;
    private final String source;
    private final String[] lines;

    public SourceHandler(String source, int beginLine, int beginColumn, int endLine, int endColumn) {
        this.source = source;
        this.beginLine = beginLine;
        this.beginColumn = beginColumn;
        this.lines = getLines(source);
        this.endLine = endLine == 0 ? lines.length : endLine;
        this.endColumn = endColumn == 0 ? lines[lines.length - 1].length() : endColumn;
        getCodePosition();
    }

    public SourceHandler(String source) {
        this(source, 1, 1, 0, 0);
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getBeginLine() {
        return beginLine;
    }

    public int getBeginColumn() {
        return beginColumn;
    }

    public String getSource() {
        return source;
    }

    public boolean isLineBetween(int beglin, int endlin, int line) {
        if (line >= beglin && line <= endlin) {
            return true;
        }
        return false;
    }

    private static String[] getLines(String source) {
        String[] lines = source.split("(?<=[\r\n|\n|\r])");
        int noNulls = 0;
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].length() == 1 && lines[i].equals("\n") && lines[i - 1].endsWith("\r")) {
                lines[i - 1] = lines[i - 1] + "\n";
                for (int j = i + 1; j < lines.length; j++) {
                    lines[j - 1] = lines[j];
                }
                noNulls++;
            }
        }
        if (noNulls > 0) {
            lines = Arrays.copyOf(lines, lines.length - noNulls);
        }
        return lines;
    }

    private void getCodePosition() {
        int offset = 0, length = 0;
        if (lines.length < endLine) {
            throw new IllegalArgumentException(
                    "Source '" + source + "' does not contain " + this + ": " + lines.length + " < " + endLine);
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

    public String[] getLines() {
        return lines;
    }

    public String getSnippet() {
        return source.substring(offset - 1, offset - 1 + length);
    }

    @Override
    public String toString() {
        return "SourceHandler [beginLine=" + beginLine + ", beginColumn=" + beginColumn + ", endLine=" + endLine
                + ", endColumn=" + endColumn + ", offset=" + offset + ", length=" + length + ", source=" + source + "]";
    }
}
