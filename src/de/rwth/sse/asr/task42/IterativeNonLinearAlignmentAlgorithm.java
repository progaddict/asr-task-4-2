package de.rwth.sse.asr.task42;

import java.util.List;

public class IterativeNonLinearAlignmentAlgorithm implements ITimeAlignmentAlgorithm {
    protected static final long INFINITY = 1000000000;
    protected static final int T0 = 2;
    protected static final int T1 = 0;
    protected static final int T2 = 2;

    protected AlgorithmRuntimeStatistics algStats;
    protected List<Integer> sample;
    protected List<Integer> signal;
    protected Alignment alignment;
    private long[][] results;
    private char[][] backpointers;

    @Override
    public String toString() {
        return "Iterative algorithm";
    }

    @Override
    public Alignment align(List<Integer> sample, List<Integer> signal) {
        final long startTime = System.currentTimeMillis();
        this.sample = sample;
        this.signal = signal;
        algStats = new AlgorithmRuntimeStatistics();
        final int tLength = signal.size();
        final int sLength = sample.size();
        results = new long[sLength][tLength];
        backpointers = new char[sLength][tLength];
        // init. first column
        results[0][0] = getDistance(0, 0);
        for (int s = 1; s < sLength; s++) {
            algStats.totalOperations++;
            results[s][0] = INFINITY;
        }
        // init. first row, s = 0
        for (int t = 1; t < tLength; t++) {
            algStats.totalOperations++;
            results[0][t] = getDistance(0, t) + results[0][t - 1] + T0;
            backpointers[0][t] = 0; // step #0
        }
        // init. second row, s = 1
        for (int t = 1; t < tLength; t++) {
            algStats.totalOperations++;
            long path0 = results[1][t - 1] + T0;
            long path1 = results[0][t - 1] + T1;
            if (path0 < path1) {
                results[1][t] = path0;
                backpointers[1][t] = 0; // step #0
            } else {
                results[1][t] = path1;
                backpointers[1][t] = 1; // step #1
            }
            results[1][t] += getDistance(1, t);
        }
        // main loop
        for (int s = 2; s < sLength; s++) {
            for (int t = 1; t < tLength; t++) {
                algStats.totalOperations++;
                long bestPath = results[s][t - 1] + T0;
                backpointers[s][t] = 0; // step # 0
                long path1 = results[s - 1][t - 1] + T1;
                if (path1 < bestPath) {
                    bestPath = path1;
                    backpointers[s][t] = 1; // step #1
                }
                long path2 = results[s - 2][t - 1] + T2;
                if (path2 < bestPath) {
                    bestPath = path2;
                    backpointers[s][t] = 2; // step #2
                }
                results[s][t] = bestPath + getDistance(s, t);
            }
        }
        // backtracking
        alignment = new Alignment();
        alignment.totalCost = results[sLength - 1][tLength - 1];
        alignment.s = new int[tLength];
        alignment.t = new int[tLength];
        int s = sLength - 1;
        int t = tLength - 1;
        while (s > 0 || t > 0) {
            alignment.s[t] = s;
            alignment.t[t] = t;
            char decision = backpointers[s][t];
            switch (decision) {
                case 1:
                    s -= 1;
                    break;
                case 2:
                    s -= 2;
                    break;
            }
            t -= 1;
        }
        alignment.s[0] = 0;
        alignment.t[0] = 0;
        final long finishTime = System.currentTimeMillis();
        algStats.runningTimeMilliseconds = finishTime - startTime;
        return alignment;
    }

    @Override
    public AlgorithmRuntimeStatistics getStats() {
        return algStats;
    }

    protected long getDistance(int s, int t) {
        algStats.totalOperations++;
        algStats.distanceCalculations++;
        return Math.abs(sample.get(s) - signal.get(t));
    }
}
