package de.rwth.sse.asr.task42;

import java.util.List;

public class RecursiveNonLinearAlignmentWithMemorizationAlgorithm extends RecursiveNonLinearAlignmentAlgorithm {
    private long[][] results;
    private boolean[][] isMemorized;

    @Override
    public String toString() {
        return "Recursive algorithm with memorization";
    }

    @Override
    public Alignment align(List<Integer> sample, List<Integer> signal) {
        final int sLength = sample.size();
        final int tLength = signal.size();
        results = new long[tLength][sLength];
        isMemorized = new boolean[tLength][sLength];
        return super.align(sample, signal);
    }

    protected long alignInternal(int s, int t) {
        algStats.totalOperations++;
        if (t < 0 || s < 0) {
            return INFINITY;
        }
        if (t == 0 && s > 0) {
            return INFINITY;
        }
        if (isMemorized[t][s]) {
            return results[t][s];
        }
        long d = getDistance(s, t);
        if (t == 0 && s == 0) {
            results[t][s] = d;
            isMemorized[t][s] = true;
            return results[t][s];
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
        results[t][s] = d + minimalCost;
        isMemorized[t][s] = true;
        return results[t][s];
    }
}