package AI.CONVOLUTION;

import java.util.ArrayList;

public class Convolutional {
	ArrayList<Double[][]> filterList = new ArrayList<>();
	ArrayList<TrainingItem> TrainingData;
	ArrayList<ConvolutionImage> data;

	public Convolutional(ArrayList<BaseImage> data) {
		Standardizer s = new Standardizer(data);
		this.data = s.DataStandardized();
		Run();
	}

	public void Run() {
		int loopIndex = 2;
		ArrayList<ConvolutionImage> ConvolutionalData = getConvolutionalData(data, loopIndex);
		TrainingData = getFinalData(ConvolutionalData);
	}

	/**
	 * @return the trainingData
	 */
	public final ArrayList<TrainingItem> getTrainingData() {
		return TrainingData;
	}

	/**
	 * @param trainingData
	 *            the trainingData to set
	 */
	public final void setTrainingData(ArrayList<TrainingItem> trainingData) {
		TrainingData = trainingData;
	}

	private ArrayList<TrainingItem> getFinalData(ArrayList<ConvolutionImage> convolutionalData) {
		ArrayList<TrainingItem> rs = new ArrayList<>();
		for (ConvolutionImage convolutionImage : convolutionalData) {
			int subSize = convolutionImage.getWidth() * convolutionImage.getHeight();
			double[] d = new double[subSize * 3];
			int count = 0;
			TrainingItem t = new TrainingItem();
			t.setId_ouput(getBinaryform(convolutionImage.getId()));
			for (int i = 0; i < convolutionImage.getWidth(); i++) {
				for (int j = 0; j < convolutionImage.getHeight(); j++) {
					d[count] = convolutionImage.getImageRed()[i][j];
					d[count + subSize] = convolutionImage.getImageRed()[i][j];
					d[count + subSize * 2] = convolutionImage.getImageRed()[i][j];
					count++;
				}
			}
			t.setData_input(d);
			rs.add(t);
		}
		return rs;
	}

	private final double[] getBinaryform(int numericalValue) {
		double[] bitarray = new double[15];
		for (int i = 0; i < 15; i++) {
			bitarray[i] = numericalValue & 0x1;
			numericalValue = numericalValue >> 1;
		}
		return bitarray;
	}

	private ArrayList<ConvolutionImage> getConvolutionalData(ArrayList<ConvolutionImage> data, int loopIndex) {
		ArrayList<ConvolutionImage> rs = data;
		Convolution c = new Convolution();
		ReLU r = new ReLU();
		Pooling p = new Pooling();
		for (int i = 0; i < loopIndex; i++) {
			ArrayList<ConvolutionImage> convolution = c.getConvvolutionData(data, filterList);
			ArrayList<ConvolutionImage> relu = r.getReLUData(convolution);
			ArrayList<ConvolutionImage> pooling = p.getPoolingData(relu);
			data = pooling;
		}
		return rs;
	}

}
