package Network;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class Main {
    private static final int INPUT_SIZE = 784;
    private static final int NET_DEPTH = 2;
    private static final int[] NODE_SIZES = {16, 16};
    private static final int OUTPUT_SIZE = 9;

    public static void main(String[] args) {
        Network network = new Network(INPUT_SIZE, NET_DEPTH, NODE_SIZES, OUTPUT_SIZE);
        double[] demo = new double[INPUT_SIZE];
        Random random = new Random();
        for (int i = 0; i < INPUT_SIZE; i++) {
            demo[i] = random.nextInt(255);
        }
        int res = network.evaluate(demo);

        String csvFilePath = "data/train.csv";

        // Read data from CSV using OpenCSV
        List<int[]> data = readDataFromCSV(csvFilePath);

        // Convert data to array
        int[][] dataArray = data.toArray(new int[0][]);

        int m = dataArray.length;
        int n = dataArray[0].length;

        // Shuffle data
        shuffleArray(dataArray);

        for (int[] datum : data) {
            System.out.println(Arrays.toString(datum));
        }

        // Split data into development and training sets
        int[][] dataDev = Arrays.copyOfRange(dataArray, 0, 1000);
        int[][] dataTrain = Arrays.copyOfRange(dataArray, 1000, m);

        // Extract labels and parameters from development and training sets
        int[] Y_dev = getColumn(dataDev, 0);
        int[][] X_dev = removeColumn(dataDev, 0);

        int[] Y_train = getColumn(dataTrain, 0);
        int[][] X_train = removeColumn(dataTrain, 0);

        // Your code continues here...
    }

    // Method to read data from CSV using OpenCSV
    private static List<int[]> readDataFromCSV(String filePath) {
        List<int[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.
            int[] line;
            String[] temp;
            while ((temp = reader.readNext()) != null) {
                line = new int[temp.length];
                for (int i = 0; i < temp.length; i++) {
                    line[i] = Integer.parseInt(temp[i]);
                }
                data.add(line);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return data;
    }

    // Method to shuffle a 2D array
    private static void shuffleArray(int[][] array) {
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int[] temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    // Method to extract a column from a 2D array
    private static int[] getColumn(int[][] array, int index) {
        int[] column = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            column[i] = array[i][index];
        }
        return column;
    }

    // Method to remove a column from a 2D array
    private static int[][] removeColumn(int[][] array, int index) {
        return Arrays.stream(array)
                .map(row -> Arrays.copyOfRange(row, 1, row.length))
                .toArray(int[][]::new);
    }
}