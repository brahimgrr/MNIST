package Network;

import java.util.Random;

public class NetworkLayer extends Layer {
    private double[] biases;
    private double[][] weights;
    private final Layer prevLayer;

    public NetworkLayer(int layerSize, Layer prevLayer) {
        super(layerSize);
        this.prevLayer = prevLayer;
        biases = new double[layerSize];
        weights = new double[layerSize][prevLayer.getLayerSize()];

        initParams();
    }
    private void initParams() {
        Random rand = new Random();
        for (int i = 0; i < biases.length; i++) {
            biases[i] = rand.nextDouble(1) - 0.5;
        }
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = rand.nextDouble(1) - 0.5;
            }
        }
        int flag = 0;
    }
    public void calculateValues() {
        if (prevLayer instanceof NetworkLayer) {
            ((NetworkLayer) prevLayer).calculateValues();
        }
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                values[i] += weights[i][j] * prevLayer.values[j];
            }
            values[i] = ReLU(values[i] + biases[i]);
        }
    }
    private double ReLU(double x) {
        return (x > 0) ? x : 0;
    }
    private double sigmoid(double x) {
        double factor = 1 + Math.exp(-x);
        return 1 / factor;
    }
}
