package AI.CONVOLUTION;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Standardizer {
	private ArrayList<BaseImage> data;
	int size = 128;

	public Standardizer(ArrayList<BaseImage> data) {
		this.data = data;
	}

	public ArrayList<ConvolutionImage> DataStandardized() {
		ArrayList<ConvolutionImage> rs = new ArrayList<>();
		for (BaseImage subImage : data) {
			Mat resizedImage = Standardized(subImage.getBaseImage());
			ConvolutionImage c = new ConvolutionImage(size, size);
			c.setId(subImage.getId());
			c.setName(subImage.getName());
			c.setImageRed(getRedArray(resizedImage));
			c.setImageGreen(getGreenArray(resizedImage));
			c.setImageBlue(getBlueArray(resizedImage));
			rs.add(c);
		}
		return rs;
	}

	private double[][] getBlueArray(Mat baseImage) {
		return MathToArray(baseImage, 2);
	}

	private double[][] getGreenArray(Mat baseImage) {
		return MathToArray(baseImage, 1);
	}

	private double[][] getRedArray(Mat baseImage) {
		return MathToArray(baseImage, 0);
	}

	private double[][] MathToArray(Mat baseImage, int idx) {
		double[][] blue = new double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (baseImage.get(i, j) != null) {
					blue[i][j] = 255;
				} else {
					blue[i][j] = baseImage.get(i, j)[idx];
				}
			}
		}
		return blue;
	}

	public Mat Standardized(Mat BaseImage) {
		Mat img = new Mat();
		Size sz = new Size(size, size);
		Imgproc.resize(BaseImage, img, sz);
		return img;
	}

	public void BlackAndWhite_Balance() {

	}

}
