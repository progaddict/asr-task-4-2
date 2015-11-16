package de.rwth.sse.asr.task42;

import java.util.ArrayList;
import java.util.List;

public class RecursiveNonLinearAlignmentAlgorithm implements ITimeAlignmentAlgorithm {
    @Override
    public List<Pair<Integer, Integer>> align(List<Integer> sample, List<Integer> signal) {
        return new ArrayList<Pair<Integer, Integer>>();
    }

    @Override
    public AlgorithmRuntimeStatistics getStats() {
        return null;
    }
}
