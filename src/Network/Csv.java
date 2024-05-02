package Network;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Csv {
    public static List<float[]> readDataFromCSV(String filePath) {
        List<float[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            float[] line;
            String[] temp;
            reader.readNext();
            while ((temp = reader.readNext()) != null) {
                line = new float[temp.length];
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

    public static void shuffleArray(float[][] array) {
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            float[] temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public static float[] getColumn(float[][] array, int index) {
        float[] column = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            column[i] = array[i][index];
        }
        return column;
    }

    public static float[][] removeColumn(float[][] array, int index) {
        return Arrays.stream(array)
                .map(row -> Arrays.copyOfRange(row, 1, row.length))
                .toArray(float[][]::new);
    }
}
