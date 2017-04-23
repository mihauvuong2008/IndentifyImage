package AI.NEURAL;

public class NNetworkConnection {
	private final Neural Sour;
	private final Neural Dest;
	private double Weigth;
	private double newWeigth;

	public NNetworkConnection(Neural Sour, Neural Dest) {
		this.Sour = Sour;
		this.Dest = Dest;
		Sour.AddConnectFromMe(this);
		Dest.AddConnectToMe(this);
	}

	public final Neural getSour() {
		return Sour;
	}

	public final Neural getDest() {
		return Dest;
	}

	public final double getWeigth() {
		return Weigth;
	}

	public final void setWeigth(double weigth) {
		this.Weigth = weigth;
	}

	public final void updateWeight() {
		Weigth = newWeigth;
	}

	public final void setNewWeigth(double newWeigth) {
		this.newWeigth = newWeigth;
	}
}