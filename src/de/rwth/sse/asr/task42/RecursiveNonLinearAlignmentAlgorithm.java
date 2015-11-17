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
        final int sLength = sample.size();
        final int tLength = signal.size();
        algStats = new AlgorithmRuntimeStatistics();
        final int alignmentLength = tLength;
        alignment = new Alignment();
        final int s[] = alignment.s = new int[alignmentLength];
        final int t[] = alignment.t = new int[alignmentLength];
        s[0] = 0;
        s[alignmentLength - 1] = sLength - 1;
        for (int i = 0; i < alignmentLength; i++) {
            t[i] = i;
        }
        alignment.totalCost = alignInternal(sLength - 1, tLength - 1);
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
        // if we are out of the grid
        // then return INFINITY
        // because this cells cannot be used to form a path
        if (t < 0 || s < 0) {
            return INFINITY;
        }
        // path should start at (0, 0)
        // otherwise the cell cannot be used for a path construction
        if (t == 0 && s > 0) {
            return INFINITY;
        }
        long d = getDistance(s, t);
        // start cell (0, 0) can be used for a path construction
        if (t == 0 && s == 0) {
            return d;
        }
        // transition T0 is always possible
        // previous checks guarantee that t > 0
        // so it can be decreased and s stays the same
        long minimalCost = alignInternal(s, t - 1) + T0;
        alignment.s[t - 1] = s; // T0
        // transition T1 is not always possible
        // so we need to check if we can decrease s by 1
        if (s > 0) {
            long transition1Cost = alignInternal(s - 1, t - 1) + T1;
            if (transition1Cost < minimalCost) {
                minimalCost = transition1Cost;
                alignment.s[t - 1] = s - 1; // T1
            }
        }
        // transition T2 is not always possible
        // so we need to check if we can decrease s by 2
        if (s > 1) {
            long transition2Cost = alignInternal(s - 2, t - 1) + T2;
            if (transition2Cost < minimalCost) {
                minimalCost = transition2Cost;
                alignment.s[t - 1] = s - 2; // T2
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
