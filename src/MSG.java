
public class MSG {
	int timeOfMSG,sourceID,senderID,hopes,MSGid;
	static int numOfMSGS;
	double signal;
	String MSG;
	
	String askLocation = "Who next to me?";
	String giveLocation = "";
	String outOfBattery = "My Battery is low";
	String inLight = "I am at the light";
	
	public MSG(int sourceID,int timeOfMSG) {				//builder
		this.MSGid = numOfMSGS;
		numOfMSGS++;
	}
	
	@Override
	public String toString() {           //Text format
		return "MSG id: " + MSGid + "\n time of MSG: " + timeOfMSG +"\n sender ID: " + sourceID + "MSG : " + MSG;
	}
	
	public void askLocation(Robot r){
		MSG = askLocation;
	}
	public void giveLightLocation (Robot r){
		MSG = inLight+ r.currLocation;
	}
	public void putLocation (Robot r){
		MSG = r.currLocation.toString();
	}
	
}
