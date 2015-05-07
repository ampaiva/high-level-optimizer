package com.ampaiva.hlo.util.view;

public interface IProgressReport {
    enum Phase {
        STARTED, BEGIN_ITEM, ENDED_ITEM, FINISHED
    }

    void onChanged(Phase phase, String id, int index, int size, int level, Object... info);
}
