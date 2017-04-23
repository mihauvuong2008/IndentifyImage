package AI.CONVOLUTION;

import java.util.ArrayList;

public class Convolution {

	private double getFilteringData(double[][] arr, double featureDetector[][], int i, int j) {
		double Sum = 0;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				Sum += arr[i + x][j + y] * featureDetector[x][y];
			}
		}
		Sum = Sum / (9 * 255);
		return Sum;
	}

	public double[][] getSingleConvolutionData(double arr[][], double featureDetector[][]) {
		int Width = arr[0].length;
		int Height = arr.length;
		double[][] rs = new double[Width - 2][Height - 2];
		for (int i = 0; i < Width - 2; i++) {
			for (int j = 0; j < Height - 2; j++) {
				rs[i][j] = getFilteringData(arr, featureDetector, i, j);
			}
		}
		return rs;
	}

	public ArrayList<ConvolutionImage> getConvvolutionData(ArrayList<ConvolutionImage> data,
			ArrayList<Double[][]> filterList) {
		// TODO Auto-generated method stub
		return null;
	}
}
