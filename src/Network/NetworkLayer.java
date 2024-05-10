package Network;

import java.util.Random;

public class NetworkLayer extends Layer {
    private final double[] biases;
    protected final double[][] weights;
    protected double[] errors;
    private final Layer prevLayer;
    private NetworkLayer nextLayer;
    private double learningRate = 0.3;

    public NetworkLayer(int layerSize, Layer prevLayer) {
        super(layerSize);
        this.prevLayer = prevLayer;
        biases = new double[layerSize];
        errors = new double[layerSize];
        weights = new double[layerSize][prevLayer.getLayerSize()];
        initParams();
    }
    public void setNextLayer(NetworkLayer nextLayer) {
        this.nextLayer = nextLayer;
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
    public void calculateError(int label) {
        assert nextLayer == null;
        for (int i = 0; i < errors.length; i++) {
            int expected = label == i ? 1 : 0;
            errors[i] = 2 * (expected - getValues()[i]) * ReLUDerivative(getValues()[i]);
        }

    }
    public void calculateError() {
        assert nextLayer != null;
        for (int i = 0; i < getLayerSize(); i++) {
            for (int j = 0; j < nextLayer.getLayerSize(); j++) {
                double errorFactor = nextLayer.errors[j] * nextLayer.weights[j][i];
                errors[i] += errorFactor;
            }
            errors[i] = ReLUDerivative(values[i]) * errors[i];
        }
    }
    public void updateParams() {
        for (int i = 0; i < weights.length; i++) { //i current neurons
            double factorBias = errors[i] *learningRate;
            biases[i] += factorBias;
            for (int j = 0; j < weights[i].length; j++) { // j previous neurons
                double factorWeight = errors[i] * prevLayer.values[j] * learningRate;
                weights[i][j] += factorWeight;
            }
        }
    }
    private double ReLU(double x) {
        return (x > 0) ? x : 0;
    }
    private double ReLUDerivative(double x) {
        return (x > 0) ? 1 : 0;
    }

    private double sigmoid(double x) {
        double factor = 1 + Math.exp(-x);
        return 1 / factor;
    }
    private double sigmoidDerivative(double x) {
        return x * (1 - x);
    }
    public void decreaseLearningRate() {
        learningRate *= 0.9;
    }
    public void reset() {
        values = new double[values.length];
        errors = new double[errors.length];
    }

}
