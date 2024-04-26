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
    }
    public void initHiddenLayers(int[] hiddenNodeSize) {
        hiddenLayers[0] = new NetworkLayer(hiddenNodeSize[0], initialLayer);
        for (int i = 1; i < hiddenLayers.length; i++) {
            hiddenLayers[i] = new NetworkLayer(hiddenNodeSize[i], hiddenLayers[i - 1]);
        }
    }
    public int evaluate(double[] data) {
        initialLayer.setValues(data);
        finalLayer.calculateValues();
        return getMaxP(softMax());
    }
    private double cost(double[] data, int label) {
        double cost = 0;
        double[] expected = new double[data.length];
        expected[label] = 1;
        initialLayer.setValues(data);
        finalLayer.calculateValues();
        double[] actual = finalLayer.getValues();
        for (int i = 0; i < actual.length; i++) {
            cost += Math.pow((actual[i] - expected[i]), 2);
        }
        return (cost / actual.length);
    }
    private double overallCost(double[][] dataset, int[] labels) {
        int pos = 0;
        double confidence = 0.0;
        double oCost = 0;
        for (int i = 0; i < labels.length; i++) {
            oCost += cost(dataset[i], labels[i]);
            if (labels[i] == finalLayer.getMaxP()) pos++;
        }
        confidence = (double) pos / labels.length;
        System.out.println("Confidence: " + confidence);
        return oCost;
    }
    private double[] softMax() {
        double[] expSum = new double[finalLayer.getLayerSize()];
        double factor = 0;
        double max_value = finalLayer.getValues()[finalLayer.getMaxP()];
        for (int i = 0; i < expSum.length; i++) {
            expSum[i] = Math.exp(finalLayer.getValues()[i] - max_value);
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
        System.out.println("Confidence: " + maxP);
        System.out.println("Res: " + curr);
        return curr;
    }
}
