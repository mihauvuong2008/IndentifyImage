package AI.NEURAL;

public class Layer {
	private final int LayerIndex;
	private double biasWeight;
	private Neural[] layer;
	private final double E = Math.E;

	public Layer(int layerIndex) {
		LayerIndex = layerIndex;
	}

	public final int getLayerIndex() {
		return LayerIndex;
	}

	public final Neural[] getLayer() {
		return layer;
	}

	public final void setLayer(Neural[] layer) {
		this.layer = layer;
	}

	public final double Transformation(double sum) {
		/**
		 * 
		 * y = f([w]); y = f( w1.a1 + w.a2 + ... + wn.an);
		 * 
		 */
		return 1 / ((1 + Math.pow(E, -1 * sum)));
	}

	public final double TotalError_Derivative(double out, double t) {
		/**
		 * y'(out1) = (1/2((t1-out1)^2 + (t2 - out2)^2 + ... + (tn - outn)^2))'
		 * = - (t1 - out1) = out1 - t1
		 */
		return out - t;
	}

	public final double Transformation_Derivative(double sum) {
		/**
		 * 
		 * y' = f'(x); y' = f[x](1-f[x]);
		 * 
		 */
		return sum * (1 - sum);
	}

	public final double Net_Total_Derivative(NNetworkConnection nnc) {
		/**
		 * y'(w1a1) = (w1*a1 + w2*a2 +...+wnan)' = a1
		 */
		return nnc.getSour().getN_Output();
	}

	public final double getBiasWeight() {
		return biasWeight;
	}

	public final void setBiasWeight(double biasWeight) {
		this.biasWeight = biasWeight;
	}

}
