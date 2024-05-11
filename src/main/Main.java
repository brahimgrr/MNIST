package main;

import data.Csv;
import network.Network;

import javax.sql.rowset.serial.SerialException;
import javax.sql.rowset.serial.SerialJavaObject;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int INPUT_SIZE = 784;
    private static final int NET_DEPTH = 1;
    private static final int[] NODE_SIZES = {10};
    private static final int OUTPUT_SIZE = 10;

    public static void main(String[] args) throws SerialException {
        Network network = new Network(INPUT_SIZE, NET_DEPTH, NODE_SIZES, OUTPUT_SIZE);

        String csvFilePath = "data/train.csv";

        List<double[]> data = Csv.readDataFromCSV(csvFilePath);

        double[][] dataArray = data.toArray(new double[0][]);

        int m = dataArray.length;
        int n = dataArray[0].length; //INPUT_SIZE = n

        double[][] dataTrain = Arrays.copyOfRange(dataArray, 0, 1000);
        double[][] dataDev = Arrays.copyOfRange(dataArray, 1000, m);

        double[] Y_train = Csv.getColumn(dataDev, 0);
        double[][] X_train = Csv.removeColumn(dataDev, 0);

        double[] Y_dev = Csv.getColumn(dataTrain, 0);
        double[][] X_dev = Csv.removeColumn(dataTrain, 0);

        network.testNetwork(X_dev, Y_dev, 0);
        for (int e = 0; e < 10; e++) {
            network.backpropagation(X_train, Y_train, 1);
            network.testNetwork(X_dev, Y_dev, (e+1));
        }
        String filename = "data/model.ser";
        Network newNetwork;
        try {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(network);

            out.close();
            file.close();

            System.out.println("Model has been serialized");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            newNetwork = (Network)in.readObject();

            in.close();
            file.close();

            System.out.println("Network has been deserialized ");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (newNetwork != null) {
            newNetwork.testNetwork(X_dev, Y_dev, 0);

        }

    }
}