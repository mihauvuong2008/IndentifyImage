package AI.NEURAL;

public class NNetWork {
	private final Layer InputLayer;
	private final Layer OutputLayer;
	private final Layer HiddenLayer;
	private final NNetworkConnection[] Network;
	private int Node_id = 0;

	public NNetWork(int iptLen, int hidLen, int outLen) {
		InputLayer = new Layer(1);
		HiddenLayer = new Layer(2);
		HiddenLayer.setBiasWeight(0.35);
		OutputLayer = new Layer(3);
		OutputLayer.setBiasWeight(0.60);
		Network = new NNetworkConnection[iptLen * hidLen + hidLen * outLen];
		CreateNetwork(iptLen, hidLen, outLen);
	}

	private final void CreateNetwork(int iptLen, int hidLen, int outLen) {
		InputLayerBuilder(iptLen);
		HiddenLayerBuilder(hidLen);
		OutputLayerBuilder(outLen);
		BuildNetwork();
	}

	private final void BuildNetwork() {
		int idx = 0;
		for (Neural h : HiddenLayer.getLayer()) {
			for (Neural i : InputLayer.getLayer()) {
				Network[idx] = new NNetworkConnection(i, h);
				idx++;
			}
		}
		for (Neural o : OutputLayer.getLayer()) {
			for (Neural h : HiddenLayer.getLayer()) {
				Network[idx] = new NNetworkConnection(h, o);
				idx++;
			}
		}
	}

	private final void InputLayerBuilder(int inputSize) {
		Neural[] NodeList = new Neural[inputSize];
		for (int i = 0; i < inputSize; i++) {
			NodeList[i] = new Neural(getNodeId());
		}
		InputLayer.setLayer(NodeList);
	}

	private final void HiddenLayerBuilder(int hiddenSize) {
		Neural[] NodeList = new Neural[hiddenSize];
		for (int i = 0; i < hiddenSize; i++) {
			NodeList[i] = new Neural(getNodeId());
		}
		HiddenLayer.setLayer(NodeList);
	}

	private final void OutputLayerBuilder(int outputSize) {
		Neural[] NodeList = new Neural[outputSize];
		for (int i = 0; i < outputSize; i++) {
			NodeList[i] = new Neural(getNodeId());
		}
		OutputLayer.setLayer(NodeList);
	}

	private final int getNodeId() {
		Node_id++;
		return Node_id - 1;
	};

	public final NNetworkConnection[] getNetwork() {
		return Network;
	}

	public final Layer getInputLayer() {
		return InputLayer;
	}

	public final Layer getOutputLayer() {
		return OutputLayer;
	}

	public final Layer getHiddenLayer() {
		return HiddenLayer;
	}

	public final void setWeight(double[] w) {
		int len = w.length;
		int i = 0;
		for (NNetworkConnection nnc : getNetwork()) {
			if (nnc.getSour().getID() >= 0) {
				nnc.setWeigth(w[i]);
				i++;
			}
		}
		OutputLayer.setBiasWeight(w[len - 1]);
		HiddenLayer.setBiasWeight(w[len - 2]);
	}
}
