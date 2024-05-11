package network;

import java.io.Serializable;

public class Layer implements Serializable {
    protected double[] values;

    public Layer(int layerSize) {
        values = new double[layerSize];
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i] / 255;
        };
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
        return curr;
    }
}
