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
    public int evaluate(float[] data) {
        initialLayer.setValues(data);
        finalLayer.calculateValues();
        return getMaxP(softMax());
    }
    private float cost(float[] data, int label) {
        float cost = 0;
        float[] expected = new float[data.length];
        expected[label] = 1;
        initialLayer.setValues(data);
        finalLayer.calculateValues();
        float[] actual = finalLayer.getValues();
        for (int i = 0; i < actual.length; i++) {
            cost += Math.pow((actual[i] - expected[i]), 2);
        }
        return (cost / actual.length);
    }
    private float overallCost(float[][] dataset, int[] labels) {
        int pos = 0;
        float confidence = 0;
        float oCost = 0;
        for (int i = 0; i < labels.length; i++) {
            oCost += cost(dataset[i], labels[i]);
            if (labels[i] == finalLayer.getMaxP()) pos++;
        }
        confidence = (float) pos / labels.length;
        System.out.println("Confidence: " + confidence);
        return oCost;
    }
    private float[] softMax() {
        float[] expSum = new float[finalLayer.getLayerSize()];
        float factor = 0;
        float e = 0;
        float max_value = finalLayer.getValues()[finalLayer.getMaxP()];
        for (int i = 0; i < expSum.length; i++) {
            e = finalLayer.getValues()[i] - max_value;
            expSum[i] = (float) Math.exp(e);
            factor += expSum[i];
        }
        for (int i = 0; i < expSum.length; i++) {
            expSum[i] = expSum[i] / factor;
        }
        return expSum;
    }
    public int getMaxP(float[] res) {
        int curr = 0;
        float maxP = 0;
        for (int i = 0; i < res.length; i++) {
            if (res[i] > maxP) {
                maxP = res[i];
                curr = i;
            }
        }
        //System.out.println("Confidence: " + maxP);
        //System.out.println("Res: " + curr);
        return curr;
    }
    private void backpropagation(float[][] dataset, int[] labels) {
        int setSize = dataset.length;
        for (int i = 0; i < setSize; i++) {
            evaluate(dataset[i]);
            float[] res = softMax();

        }
    }
}
