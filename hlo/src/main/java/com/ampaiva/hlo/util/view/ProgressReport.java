package com.ampaiva.hlo.util.view;

public class ProgressReport implements IProgressReport {
    public static final int UNLIMITED = -1;
    private final int maxLevel;

    public ProgressReport(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public ProgressReport() {
        this(UNLIMITED);
    }

    private String getIdent(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("   ");
        }
        return sb.toString();
    }

    public void onChanged(Phase phase, String id, int index, int size, int level, Object... info) {
        if (maxLevel == UNLIMITED || level <= maxLevel) {
            StringBuilder sb = new StringBuilder();
            sb.append(getIdent(level)).append(phase).append(" ").append(level).append(" ").append(id);
            if (phase == Phase.BEGIN_ITEM || phase == Phase.ENDED_ITEM) {
                sb.append(" ").append(index + 1).append("/").append(size);
            }
            if (info.length > 0) {
                sb.append(" ");
                for (Object obj : info) {
                    sb.append(obj.toString());
                }
            }
            print(sb.toString());
        }
    }

    public void print(String text) {
        System.out.println(text);
    }
}
