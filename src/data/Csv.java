package data;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Csv {
    public static List<double[]> readDataFromCSV(String filePath) {
        List<double[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            double[] line;
            String[] temp;
            reader.readNext();
            while ((temp = reader.readNext()) != null) {
                line = new double[temp.length];
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

    public static void shuffleArray(double[][] array) {
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            double[] temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public static double[] getColumn(double[][] array, int index) {
        double[] column = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            column[i] = array[i][index];
        }
        return column;
    }

    public static double[][] removeColumn(double[][] array, int index) {
        return Arrays.stream(array)
                .map(row -> Arrays.copyOfRange(row, 1, row.length))
                .toArray(double[][]::new);
    }
}
