
public class MSG {
	int timeOfMSG,sourceID,senderID,hopes,MSGid;
	static int numOfMSGS;
	double signal;
	String MSG;
	
	public MSG(String msg,double signal,int sourceID,int timeOfMSG) {
		this.MSG = msg;
		this.signal = signal;
		this.MSGid = numOfMSGS;
		numOfMSGS++;
	}

	
}
