package com.example.mnist.network;

import java.io.Serializable;

public class Network implements Serializable {
    private final Layer initialLayer;
    private final NetworkLayer[] hiddenLayers;
    private final NetworkLayer finalLayer;

    public Network(int inputSize, int hiddenSize,int[] hiddenNodeSize,int outputSize) {
        initialLayer = new Layer(inputSize);
        hiddenLayers = new NetworkLayer[hiddenSize];
        initHiddenLayers(hiddenNodeSize);
        finalLayer = new NetworkLayer(outputSize, hiddenLayers[hiddenLayers.length - 1]);
        hiddenLayers[hiddenLayers.length - 1].setNextLayer(finalLayer);
    }
    public void initHiddenLayers(int[] hiddenNodeSize) {
        hiddenLayers[0] = new NetworkLayer(hiddenNodeSize[0], initialLayer);
        for (int i = 1; i < hiddenLayers.length; i++) {
            hiddenLayers[i] = new NetworkLayer(hiddenNodeSize[i], hiddenLayers[i - 1]);
            hiddenLayers[i-1].setNextLayer(hiddenLayers[i]);
        }
    }
    public double[] evaluate(double[] data) {
        double[] softMax;
        int resPosition;
        initialLayer.setValues(data);
        finalLayer.calculateValues();
        softMax = softMax();
        resPosition = getMaxP(softMax);
        return new double[]{resPosition, softMax[resPosition]};
    }
    private double[] softMax() {
        double[] sum = new double[finalLayer.getLayerSize()];
        double factor = 0;
        for (int i = 0; i < sum.length; i++) {
            sum[i] = finalLayer.getValues()[i];
            factor += finalLayer.getValues()[i];
        }
        for (int i = 0; i < sum.length; i++) {
            sum[i] = sum[i] / factor;
        }
        return sum;
    }
    public int getMaxP(double[] res) {
        int curr = 0;
        double maxP = 0;
        for (int i = 0; i < res.length; i++) {
            if (res[i] > maxP) {
                maxP = res[i];
                curr = i;
            }
        }
        return curr;
    }
    public void backpropagation(double[][] dataset, double[] labels, int epochs) {
        int setSize = dataset.length;
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < setSize; i++) {
                reset();
                evaluate(dataset[i]);
                finalLayer.calculateError((int) labels[i]);
                for (int l = hiddenLayers.length; l > 0; l--) {
                    hiddenLayers[l-1].calculateError();
                }
                for (int l = 0; l < hiddenLayers.length; l++) {
                    hiddenLayers[l].updateParams();
                }
                finalLayer.updateParams();
            }
            decreaseLearningRate();
        }
    }
    public void testNetwork(double[][] X_dev, double[] Y_dev, int epoch) {
        int[] res = new int[10];
        int[] exp = new int[10];
        double error = 0;
        for (int i = 0; i < X_dev.length; i++) {
            exp[(int) Y_dev[i]] += 1;
            res[(int) evaluate(X_dev[i])[0]] += 1;
        }
        for (int i = 0; i < res.length; i++) {
            error += Math.max(0, res[i] - exp[i]);
        }
        for (int i = 0; i <= 9; i++) {
            System.out.printf("Case " + i + ": %5d - Exp: %5d\n", res[i], exp[i]);
        }
        System.out.printf("Done epoch: %4d\n", epoch);
        System.out.printf("Network accuracy: %.2f %%\n", (Y_dev.length - error) * 100/ Y_dev.length);
        System.out.println(" ");
    }
    private void decreaseLearningRate() {
        finalLayer.decreaseLearningRate();
        for (int l = 0; l < hiddenLayers.length; l++) {
            hiddenLayers[l].decreaseLearningRate();
        }
    }
    private void reset() {
        initialLayer.values = new double[initialLayer.getLayerSize()];
        finalLayer.reset();
        for (NetworkLayer hiddenLayer : hiddenLayers) {
            hiddenLayer.reset();
        }
    }
}
