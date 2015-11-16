package de.rwth.sse.asr.task42;

import java.util.List;

public interface ITimeAlignmentAlgorithm {
    List<Pair<Integer, Integer>> align(List<Integer> sample, List<Integer> signal);

    AlgorithmRuntimeStatistics getStats();
}
