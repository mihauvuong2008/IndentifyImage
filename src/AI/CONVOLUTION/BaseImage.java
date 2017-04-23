package AI.CONVOLUTION;

import org.opencv.core.Mat;

public class BaseImage {
	private int id;
	private String name;
	private final Mat BaseImage;

	public BaseImage(Mat BaseImage) {
		this.BaseImage = BaseImage;
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

	public final Mat getBaseImage() {
		return BaseImage;
	}

}
