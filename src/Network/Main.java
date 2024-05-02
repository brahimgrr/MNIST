package Network;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int INPUT_SIZE = 784;
    private static final int NET_DEPTH = 2;
    private static final int[] NODE_SIZES = {16, 16};
    private static final int OUTPUT_SIZE = 9;

    public static void main(String[] args) {
        Network network = new Network(INPUT_SIZE, NET_DEPTH, NODE_SIZES, OUTPUT_SIZE);

        String csvFilePath = "data/train.csv";

        List<float[]> data = Csv.readDataFromCSV(csvFilePath);

        float[][] dataArray = data.toArray(new float[0][]);

        int m = dataArray.length;
        int n = dataArray[0].length;

        Csv.shuffleArray(dataArray);

        float[][] dataDev = Arrays.copyOfRange(dataArray, 0, 1000);
        float[][] dataTrain = Arrays.copyOfRange(dataArray, 1000, m);

        float[] Y_dev = Csv.getColumn(dataDev, 0);
        float[][] X_dev = Csv.removeColumn(dataDev, 0);

        float[] Y_train = Csv.getColumn(dataTrain, 0);
        float[][] X_train = Csv.removeColumn(dataTrain, 0);

        int[] res = new int[9];
        for (int i = 0; i < 1000; i++) {
            res[network.evaluate(X_dev[i])] += 1;
        }
        for (int i = 0; i < 9; i++) {
            System.out.printf("Case " + (i + 1) + ": %d\n", res[i]);
        }
    }
}