package AI.CONVOLUTION;

public class Filter {
	public double[][] filter1() {
		double[][] s = new double[][] { { 1, 1, 1 }, { 1, -8, 1 }, { 1, 1, 1 } };
		return s;
	}

	public double[][] filter2() {
		double[][] s = new double[][] { { 1, 1, 1 }, { 0, 0, 0 }, { 1, 1, 1 } };
		return s;
	}

	public double[][] filter3() {
		double[][] s = new double[][] { { 1, 0, 1 }, { 1, 0, 1 }, { 1, 0, 1 } };
		return s;
	}

	public double[][] filter4() {
		double[][] s = new double[][] { { 0, 1, 1 }, { 1, 0, 1 }, { 1, 1, 0 } };
		return s;
	}
}
