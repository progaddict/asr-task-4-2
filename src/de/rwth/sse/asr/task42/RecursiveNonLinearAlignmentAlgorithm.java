package de.rwth.sse.asr.task42;

import java.util.List;

public class RecursiveNonLinearAlignmentAlgorithm implements ITimeAlignmentAlgorithm {
    protected static final long INFINITY = 1000000000;
    protected static final int T0 = 2;
    protected static final int T1 = 0;
    protected static final int T2 = 2;

    protected AlgorithmRuntimeStatistics algStats;
    protected List<Integer> sample;
    protected List<Integer> signal;
    protected Alignment alignment;

    @Override
    public String toString() {
        return "Naive recursive algorithm";
    }

    @Override
    public Alignment align(List<Integer> sample, List<Integer> signal) {
        final long startTime = System.currentTimeMillis();
        this.sample = sample;
        this.signal = signal;
        algStats = new AlgorithmRuntimeStatistics();
        final int alignmentLength = signal.size();
        alignment = new Alignment();
        final int s[] = alignment.s = new int[alignmentLength];
        final int t[] = alignment.t = new int[alignmentLength];
        s[0] = 0;
        t[0] = 0;
        s[alignmentLength - 1] = signal.size() - 1;
        t[alignmentLength - 1] = alignmentLength - 1;
        for (int i = 0; i < alignmentLength; i++) {
            t[i] = i;
        }
        alignment.totalCost = alignInternal(sample.size() - 1, signal.size() - 1);
        final long finishTime = System.currentTimeMillis();
        algStats.runningTimeMilliseconds = finishTime - startTime;
        return alignment;
    }

    @Override
    public AlgorithmRuntimeStatistics getStats() {
        return algStats;
    }

    protected long alignInternal(int s, int t) {
        algStats.totalOperations++;
        if (t < 0 || s < 0) {
            return INFINITY;
        }
        if (t == 0 && s > 0) {
            return INFINITY;
        }
        long d = getDistance(s, t);
        if (t == 0 && s == 0) {
            return d;
        }
        long minimalCost = alignInternal(s, t - 1) + T0;
        alignment.s[t - 1] = s;
        if (s > 0) {
            long transition1Cost = alignInternal(s - 1, t - 1) + T1;
            if (transition1Cost < minimalCost) {
                minimalCost = transition1Cost;
                alignment.s[t - 1] = s - 1;
            }
        }
        if (s > 1) {
            long transition2Cost = alignInternal(s - 2, t - 1) + T2;
            if (transition2Cost < minimalCost) {
                minimalCost = transition2Cost;
                alignment.s[t - 1] = s - 2;
            }
        }
        return d + minimalCost;
    }

    protected long getDistance(int s, int t) {
        algStats.totalOperations++;
        algStats.distanceCalculations++;
        return Math.abs(sample.get(s) - signal.get(t));
    }
}
