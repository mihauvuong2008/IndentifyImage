package AI.NEURAL;

public class Neural {

	private final int ID;
	private double N_Output;
	private double error_out_net;
	private NNetworkConnection[] getConnectToMe;
	private NNetworkConnection[] getConnectFromMe;

	public Neural(int ID) {
		this.ID = ID;
	}

	public final int getID() {
		return ID;
	}

	public final double getN_Output() {
		return N_Output;
	}

	public final void setN_Output(double N_Output) {
		this.N_Output = N_Output;
	}

	public final double getError() {
		return error_out_net;
	}

	public final void setError(double error) {
		this.error_out_net = error;
	}

	public NNetworkConnection getConnection(Neural n) {
		for (NNetworkConnection nNetworkConnection : getGetConnectionFromMe()) {
			if (nNetworkConnection.getDest().equals(n)) {
				return nNetworkConnection;
			}
		}
		return null;
	}

	public void AddConnectToMe(NNetworkConnection push) {
		if (getConnectToMe == null)
			getConnectToMe = new NNetworkConnection[0];
		int len = getConnectToMe.length;
		NNetworkConnection[] tmp = new NNetworkConnection[len + 1];
		System.arraycopy(getConnectToMe, 0, tmp, 0, len);
		tmp[len] = push;
		getConnectToMe = tmp;
	}

	public void AddConnectFromMe(NNetworkConnection push) {
		if (getConnectFromMe == null)
			getConnectFromMe = new NNetworkConnection[0];
		int len = getConnectFromMe.length;
		NNetworkConnection[] tmp = new NNetworkConnection[len + 1];
		System.arraycopy(getConnectFromMe, 0, tmp, 0, len);
		tmp[len] = push;
		getConnectFromMe = tmp;
	}

	public final NNetworkConnection[] getGetConnectionToMe() {
		return getConnectToMe;
	}

	public final void setGetConnectionToMe(NNetworkConnection[] getConnectToMe) {
		this.getConnectToMe = getConnectToMe;
	}

	public final NNetworkConnection[] getGetConnectionFromMe() {
		return getConnectFromMe;
	}

	public final void setGetConnectionFromMe(NNetworkConnection[] getConnectFromMe) {
		this.getConnectFromMe = getConnectFromMe;
	}

}
