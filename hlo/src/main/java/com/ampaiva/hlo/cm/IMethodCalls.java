package com.ampaiva.hlo.cm;

import java.util.List;

public interface IMethodCalls {

    public abstract List<String> getMethodSources();

    public abstract List<String> getMethodNames();

    public abstract List<List<Integer>> getMethodPositions();

    public abstract List<List<String>> getSequences();

}