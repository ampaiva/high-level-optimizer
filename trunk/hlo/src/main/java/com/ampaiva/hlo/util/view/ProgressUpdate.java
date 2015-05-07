package com.ampaiva.hlo.util.view;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ampaiva.hlo.util.view.IProgressReport.Phase;

public class ProgressUpdate implements IProgressUpdate {
    private static Map<Thread, LinkedList<ProgressUpdate>> map = new ConcurrentHashMap<Thread, LinkedList<ProgressUpdate>>();

    private final String id;
    private final int size;
    private final int level;
    private int index;
    private int prevIndex = -1;
    private Object[] info;
    private final IProgressReport progressReport;

    private ProgressUpdate(IProgressReport progressReport, String id, int size, int level) {
        this.id = id;
        this.progressReport = progressReport;
        this.size = size;
        this.level = level;
    }

    private static LinkedList<ProgressUpdate> getLinkedList() {
        LinkedList<ProgressUpdate> current = map.get(Thread.currentThread());
        return current;
    }

    private static LinkedList<ProgressUpdate> setLinkedList(LinkedList<ProgressUpdate> linkedList) {
        return map.put(Thread.currentThread(), linkedList);
    }

    private static LinkedList<ProgressUpdate> removeLinkedList() {
        return map.remove(Thread.currentThread());
    }

    public static IProgressUpdate start(IProgressReport progressReport, String id, int size) {
        LinkedList<ProgressUpdate> linkedList = getLinkedList();
        if (linkedList == null) {
            linkedList = new LinkedList<ProgressUpdate>();
            setLinkedList(linkedList);
        } else {
            throw new IllegalArgumentException("Progress report already registered " + progressReport);
        }
        return addNew(progressReport, id, size);
    }

    public static IProgressUpdate start(String id, int size) {
        LinkedList<ProgressUpdate> linkedList = getLinkedList();
        if (linkedList != null) {
            return addNew(linkedList.getLast().progressReport, id, size);
        }
        // Fake
        return new IProgressUpdate() {

            public void endIndex(Object... info) {
            }

            public void beginIndex(Object... info) {
            }
        };
    }

    private static IProgressUpdate addNew(IProgressReport progressReport, String id, int size) {
        LinkedList<ProgressUpdate> linkedList = getLinkedList();
        ProgressUpdate progressUpdate = new ProgressUpdate(progressReport, id, size, linkedList.size());
        linkedList.add(progressUpdate);
        progressUpdate.report(Phase.STARTED);
        return progressUpdate;
    }

    public void beginIndex(Object... info) {
        finishChild();
        if (prevIndex == index) {
            endIndex();
        }
        prevIndex = index;

        this.info = info;
        report(Phase.BEGIN_ITEM, info);
    }

    public void endIndex(Object... info) {
        finishChild();
        if (info.length == 0) {
            info = this.info;
        }
        report(Phase.ENDED_ITEM, info);
        prevIndex = index;
        index++;
        if (index == size) {
            finish();
        }
    }

    private void finish() {
        report(Phase.FINISHED);
        LinkedList<ProgressUpdate> linkedList = getLinkedList();
        linkedList.remove(this);
        if (linkedList.size() == 0) {
            removeLinkedList();
        }
    }

    private void finishChild() {
        LinkedList<ProgressUpdate> linkedList = getLinkedList();
        ProgressUpdate last;
        while ((last = linkedList.getLast()) != this) {
            last.endIndex();
            if (last.index < last.size) {
                last.finish();
            }
        }
    }

    private void report(Phase phase, Object... info) {
        progressReport.onChanged(phase, id, index, size, level, info);
    }
}
