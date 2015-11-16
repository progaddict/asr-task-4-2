package de.rwth.sse.asr.task42;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Task42 {
    public static void main(String[] args) {
        List<Integer> sample = readIntegersSequence("ref.dat");
        System.out.println("Sample length =  " + sample.size());
        List<Integer> signal = readIntegersSequence("test.dat");
        System.out.println("Signal length =  " + signal.size());
        ITimeAlignmentAlgorithm[] algorithms = new ITimeAlignmentAlgorithm[]{
                new RecursiveNonLinearAlignmentAlgorithm(),
        };
        for (int i = 0; i < algorithms.length; i++) {
            ITimeAlignmentAlgorithm algorithm = algorithms[i];
            System.out.println(algorithm);
            Alignment alignment = algorithm.align(sample, signal);
            System.out.print("alignment:");
            for (int j = 0; j < alignment.t.length; j++) {
                System.out.print(" (" + alignment.t[j] + "; " + alignment.s[j] + ")");
            }
            System.out.println();
            System.out.println("alignment cost = " + alignment.totalCost);
            AlgorithmRuntimeStatistics stats = algorithm.getStats();
            System.out.println("running time = " + stats.runningTimeMilliseconds);
            System.out.println("total operations = " + stats.totalOperations);
            System.out.println("distance calculations = " + stats.distanceCalculations);
        }
    }

    private static List<Integer> readIntegersSequence(String filePath) {
        List<Integer> result = new ArrayList<Integer>();
        Scanner scanner;
        try {
            scanner = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        }
        while (scanner.hasNextInt()) {
            int value = scanner.nextInt();
            result.add(value);
        }
        return result;
    }
}
