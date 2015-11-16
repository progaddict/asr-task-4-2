package de.rwth.sse.asr.task42;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Task42 {
    private static final int MAX_SEQUENCE_LENGTH = 1000;

    public static void main(String[] args) {
        int[] sample = new int[MAX_SEQUENCE_LENGTH];
        int sampleLength = readIntegersSequence("ref.dat", sample);
        int[] signal = new int[MAX_SEQUENCE_LENGTH];
        int signalLength = readIntegersSequence("test.dat", signal);
    }

    private static int readIntegersSequence(String filePath, int[] sequence) {
        int sequenceLength = 0;
        Scanner scanner;
        try {
            scanner = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
        while (scanner.hasNextInt()) {
            sequence[sequenceLength++] = scanner.nextInt();
        }
        return sequenceLength;
    }
}
