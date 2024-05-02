package Network;

import java.util.Random;

public class NetworkLayer extends Layer {
    private float[] biases;
    private float[][] weights;
    private final Layer prevLayer;

    public NetworkLayer(int layerSize, Layer prevLayer) {
        super(layerSize);
        this.prevLayer = prevLayer;
        biases = new float[layerSize];
        weights = new float[layerSize][prevLayer.getLayerSize()];

        initParams();
    }
    private void initParams() {
        Random rand = new Random();
        for (int i = 0; i < biases.length; i++) {
            biases[i] = (float) (rand.nextFloat(1) - 0.5);
        }
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = (float) (rand.nextFloat(1) - 0.5);
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
            values[i] = sigmoid(values[i] + biases[i]);
        }
    }
    private float ReLU(float x) {
        return (x > 0) ? x : 0;
    }

    private float sigmoid(float x) {
        float factor = (float) (1 + Math.exp(-x));
        return 1 / factor;
    }
}
