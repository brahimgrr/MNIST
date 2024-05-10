package Network;

public class Network {
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
    public int evaluate(double[] data) {
        initialLayer.setValues(data);
        finalLayer.calculateValues();
        return getMaxP(softMax());
    }

    private double[] softMax() {
        double[] expSum = new double[finalLayer.getLayerSize()];
        double factor = 0;
        double e = 0;
        double max_value = finalLayer.getValues()[finalLayer.getMaxP()];
        for (int i = 0; i < expSum.length; i++) {
            e = finalLayer.getValues()[i] - max_value;
            expSum[i] = Math.exp(e);
            factor += expSum[i];
        }
        for (int i = 0; i < expSum.length; i++) {
            expSum[i] = expSum[i] / factor;
        }
        return expSum;
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
                    hiddenLayers[l].decreaseLearningRate();
                }
                finalLayer.updateParams();
                finalLayer.decreaseLearningRate();

            }
        }
    }
    private void reset() {
        initialLayer.values = new double[initialLayer.getLayerSize()];
        finalLayer.reset();
        for (int i = 0; i < hiddenLayers.length; i++) {
            hiddenLayers[i].reset();
        }
    }
}
