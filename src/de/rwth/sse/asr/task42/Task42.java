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
