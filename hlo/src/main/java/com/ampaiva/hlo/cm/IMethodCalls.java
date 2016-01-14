package com.ampaiva.hlo.cm;

import java.util.List;

import com.ampaiva.hlo.util.SourceHandler;

public interface IMethodCalls {

    public abstract List<SourceHandler> getMethodSources();

    public abstract List<String> getMethodNames();

    public abstract List<List<Integer>> getMethodPositions();

    public abstract List<List<String>> getSequences();

}