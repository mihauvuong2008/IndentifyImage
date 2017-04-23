package AI.NEURAL;

import java.util.ArrayList;
import java.util.Random;

import AI.CONVOLUTION.TrainingItem;

public final class ANN {
	private int LoopLimit;
	private final Random bigbang;
	private NNetWork nnetwork;
	private int loopIndex = 5000;
	private double eta = 0.4;
	private int inputlen = 30;
	private int hiddenlen = 35;
	private int outputlen = 15;
	ArrayList<double[]> inputData;
	ArrayList<double[]> ouputData;

	public ANN(Random bigbang) {
		this.bigbang = bigbang;
		nnetwork = new NNetWork(inputlen, hiddenlen, outputlen);
		setRandomWeight(nnetwork.getNetwork(), bigbang);
	}

	public final void ANN_RUN() {
		showOutput();
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < loopIndex; i++) {
			int x = 0;
			for (double[] is : inputData) {
				Lantruyen(is);
				LantruyenNguoc(ouputData.get(x));
				x++;
			}
		}
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);
	}

	private void showOutput() {
		for (double[] ds : ouputData) {
			String rs = "";
			for (double d : ds) {
				rs += (int) d + "";
			}
			System.out.println(rs);
		}
	}

	private final void LantruyenNguoc(double[] t) {
		updateOutputLayer(t);
		updateHiddenLayer();
		updateWeight();
	}

	private final void updateWeight() {
		for (NNetworkConnection nnc : nnetwork.getNetwork()) {
			nnc.updateWeight();
		}
	}

	private final void updateHiddenLayer() {
		for (Neural n : nnetwork.getHiddenLayer().getLayer()) {
			double dnet_n = nnetwork.getHiddenLayer().Transformation_Derivative(n.getN_Output());
			for (NNetworkConnection nnc : n.getGetConnectionToMe()) {
				/**
				 * w2+ = w2 + eta* E'(w2);
				 * 
				 * @ delta(w2) = E'(w2) =
				 * E_Total'(Transform(Net_sum(Transform(Netsum(w2))))) . =
				 * E_Total'(Transform(Net_sum()))*Transform'(Netsum(w2))
				 * E'(Trans(w1)) = E1'(Trans(w1)) + E2'(Trans(w2)) + ... +
				 * E_lenhidden;
				 * 
				 * 
				 */
				double Total_error = 0;
				// for (Neural n_o : nnetwork.getOutputLayer().getLayer()) {
				// Total_error += (double) n_o.getError() *
				// n.getConnection(n_o).getWeigth();
				// }
				for (NNetworkConnection nnc_ : n.getGetConnectionFromMe()) {
					Total_error += (double) nnc_.getDest().getError() * nnc_.getWeigth();
				}
				double delta = (double) nnc.getSour().getN_Output() * dnet_n * Total_error;
				nnc.setNewWeigth(nnc.getWeigth() - eta * delta);
			}
		}
	}

	private final void updateOutputLayer(double[] t) {
		int idx = 0;
		for (Neural n : nnetwork.getOutputLayer().getLayer()) {
			double error = nnetwork.getOutputLayer().TotalError_Derivative(n.getN_Output(), t[idx])
					* nnetwork.getOutputLayer().Transformation_Derivative(n.getN_Output());
			n.setError(error);
			for (NNetworkConnection nnc : n.getGetConnectionToMe()) {
				/**
				 * w1+ = w1 + eta* E'(w1);
				 * 
				 * @delta(w1) = E'(w1) = E'(Transform(Net_sum(w1))) = delta(w1)
				 * = E'(Trans) * Trans'(Net_sum) * Net_sum'(w1)
				 */
				double delta = error * nnetwork.getOutputLayer().Net_Total_Derivative(nnc);
				nnc.setNewWeigth(nnc.getWeigth() - eta * delta);
			}
			idx++;
		}
	}

	public final void Lantruyen(double[] is) {
		setInput(is);
		setHidden();
		setOutput();
	}

	public final String getOutput() {
		String result = "";
		for (Neural n : nnetwork.getOutputLayer().getLayer()) {
			result += (double) n.getN_Output() + " ";
		}
		return result;
	}

	private final void setOutput() {
		Layer Output = nnetwork.getOutputLayer();
		for (Neural n : Output.getLayer()) {
			double Sum = 0;
			for (NNetworkConnection cnn : n.getGetConnectionToMe()) {
				Sum += (cnn.getWeigth() * cnn.getSour().getN_Output());
			}
			n.setN_Output(Output.Transformation(Sum + Output.getBiasWeight()));
		}
	}

	private final void setHidden() {
		Layer Hidden = nnetwork.getHiddenLayer();
		for (Neural n : Hidden.getLayer()) {
			double Sum = 0;
			for (NNetworkConnection cnn : n.getGetConnectionToMe()) {
				Sum += ((cnn.getWeigth() * cnn.getSour().getN_Output()));
			}
			n.setN_Output(Hidden.Transformation(Sum + Hidden.getBiasWeight()));
		}
	}

	private final void setInput(double[] is) {
		for (int i = 0; i < is.length; i++) {
			nnetwork.getInputLayer().getLayer()[i].setN_Output(is[i]);
		}
	}

	private final void setRandomWeight(NNetworkConnection[] network, Random bigbang2) {
		// double w = 0.15;
		for (NNetworkConnection nNetworkConnection : network) {
			// if (w == 0.35)
			// w += 0.05;
			// nNetworkConnection.setWeigth(/* bigbang2.nextDouble() */w);
			// w = w + 0.05;
			nNetworkConnection.setWeigth(bigbang2.nextDouble());
		}
	}

	public final String showarr(int[] des2) {
		String g_ = " GENE: ";
		for (double d : des2) {
			g_ += d + " - ";
		}
		return g_;
	}

	public final void showNetwork() {

		ShowLayer(nnetwork.getInputLayer(), nnetwork, "====INPUT LAYER====");
		ShowLayer(nnetwork.getHiddenLayer(), nnetwork, "====HIDDEN LAYER====");
		ShowLayer(nnetwork.getOutputLayer(), nnetwork, "====OUTPUT LAYER====");
	}

	final void ShowLayer(Layer l, NNetWork nnetwork, String NAME) {
		System.out.println(NAME);
		for (Neural n : l.getLayer()) {
			System.out.println("-NEURAL - ID: " + n.getID() + " - VALUE: " + n.getN_Output());
			for (NNetworkConnection nnc : nnetwork.getNetwork()) {
				if (nnc.getDest().getID() == n.getID()) {
					System.out.println("CONNECTION: FROM: " + nnc.getSour().getID() + " TO: " + nnc.getDest().getID()
							+ " - WEIGHT: " + nnc.getWeigth());
				}
			}
		}
	}

	public final int getLimit() {
		return LoopLimit;
	}

	public final double getEta() {
		return eta;
	}

	public final void setEta(double eta) {
		this.eta = eta;
	}

	public final void setLoopIndex(Integer valueOf) {
		loopIndex = valueOf;
	}

	public final NNetWork getNnetwork() {
		return nnetwork;
	}

	public final double OutputError(double[] t) {
		double sum = 0;
		int x = 0;
		for (Neural n : nnetwork.getOutputLayer().getLayer()) {
			sum += (double) Math.pow(n.getN_Output() - t[x], 2);
			x++;
		}
		return sum;
	}

	public void setTrainingData(ArrayList<TrainingItem> trainingData) {
		inputData = new ArrayList<>();
		ouputData = new ArrayList<>();
		for (TrainingItem trainingItem : trainingData) {
			inputData.add(trainingItem.getData_input());
			ouputData.add(trainingItem.getId_ouput());
		}
	}

}
