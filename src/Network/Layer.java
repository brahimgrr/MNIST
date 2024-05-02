package Network;

public class Layer {
    protected float[] values;

    public Layer(int layerSize) {
        values = new float[layerSize];
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }

    public int getLayerSize() {
        return values.length;
    }

    public int getMaxP() {
        int curr = 0;
        float maxP = 0;
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
