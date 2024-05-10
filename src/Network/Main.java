package Network;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final int INPUT_SIZE = 784;
    private static final int NET_DEPTH = 1;
    private static final int[] NODE_SIZES = {56};
    private static final int OUTPUT_SIZE = 10;

    public static void main(String[] args) {
        Network network = new Network(INPUT_SIZE, NET_DEPTH, NODE_SIZES, OUTPUT_SIZE);

        String csvFilePath = "data/train.csv";

        List<double[]> data = Csv.readDataFromCSV(csvFilePath);

        double[][] dataArray = data.toArray(new double[0][]);

        int m = dataArray.length;
        int n = dataArray[0].length;

        //Csv.shuffleArray(dataArray);

        double[][] dataTrain = Arrays.copyOfRange(dataArray, 0, 1000);
        double[][] dataDev = Arrays.copyOfRange(dataArray, 1000, m);

        double[] Y_train = Csv.getColumn(dataDev, 0);
        double[][] X_train = Csv.removeColumn(dataDev, 0);

        double[] Y_dev = Csv.getColumn(dataTrain, 0);
        double[][] X_dev = Csv.removeColumn(dataTrain, 0);
        Scanner scanner = new Scanner(System.in);

        for (int e = 0; e < 20; e++) {
            network.backpropagation(X_train, Y_train, 1);

            int[] res = new int[10];
            int[] exp = new int[10];
            double accuracy = 0;
            for (int i = 0; i < X_train.length; i++) {
                exp[(int) Y_train[i]] += 1;
                res[network.evaluate(X_train[i])] += 1;
            }
            for (int i = 0; i < res.length; i++) {
                accuracy = Math.abs(exp[i] - res[i]);
            }
            for (int i = 0; i < 9; i++) {
                System.out.printf("Case " + (i + 1) + ": %4d - Exp: %4d\n", res[i], exp[i]);
            }
            System.out.printf("Done epoch %4d\n", (e+1));
            System.out.printf("Network accuracy: %.2f %%\n", accuracy * 100/ Y_train.length);

            System.out.println(" ");
        }
    }
}