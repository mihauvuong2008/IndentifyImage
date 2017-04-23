package View;
import org.opencv.core.Mat;

public class MatImage {
	String Name;
	Mat mat;

	public MatImage(String Name, Mat img) {
		this.Name = Name;
		this.mat = img;
	}

	public final Mat getMat() {
		return mat;
	}

	public final void setMat(Mat mat) {
		this.mat = mat;
	}

	public String toString() {
		return Name;
	}
}
