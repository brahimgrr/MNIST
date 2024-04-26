package Network;

public class Layer {
    protected double[] values;

    public Layer(int layerSize) {
        values = new double[layerSize];
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    public int getLayerSize() {
        return values.length;
    }

    public int getMaxP() {
        int curr = 0;
        double maxP = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] > maxP) {
                maxP = values[i];
                curr = i;
            }
        }
        //System.out.println("Confidence: " + maxP);
        //System.out.println("Res: " + curr);
        return curr;
    }
}
