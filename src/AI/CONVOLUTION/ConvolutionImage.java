package AI.CONVOLUTION;

public class ConvolutionImage {

	int id;
	String name;
	double[][] ImageRed;
	double[][] ImageGreen;
	double[][] ImageBlue;
	private int width;// (x)
	private int height;// (y)

	public ConvolutionImage(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public final int getId() {
		return id;
	}

	public final void setId(int id) {
		this.id = id;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final double[][] getImageRed() {
		return ImageRed;
	}

	public final void setImageRed(double[][] imageRed) {
		ImageRed = imageRed;
	}

	public final double[][] getImageGreen() {
		return ImageGreen;
	}

	public final void setImageGreen(double[][] imageGreen) {
		ImageGreen = imageGreen;
	}

	public final double[][] getImageBlue() {
		return ImageBlue;
	}

	public final void setImageBlue(double[][] imageBlue) {
		ImageBlue = imageBlue;
	}

	public final int getWidth() {
		return width;
	}

	public final void setWidth(int width) {
		this.width = width;
	}

	public final int getHeight() {
		return height;
	}

	public final void setHeight(int height) {
		this.height = height;
	}

}
