
public class MSG {
	int timeOfMSG,sourceID,senderID,hopes,MSGid;
	static int numOfMSGS;
	double signal;
	String MSG;
	
	public MSG(String msg,double signal,int sourceID,int timeOfMSG) {				//builder
		this.MSG = msg;
		this.signal = signal;
		this.MSGid = numOfMSGS;
		numOfMSGS++;
	}
	
	@Override
	public String toString() {           //Text format
		return "MSG id: " + MSGid + "\n time of MSG: " + timeOfMSG +"\n sender ID: " + sourceID + "MSG : " + MSG;
	}

	
}
