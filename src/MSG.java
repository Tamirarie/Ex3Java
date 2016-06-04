
public class MSG {
	int timeOfMSG,sourceID,senderID,hopes,MSGid;
	static int numOfMSGS;
	double signal;
	String MSG;
	
	String askLocation = "Who next to me?";
	//String giveLocation = "";
	String outOfBattery = "My Battery is low";
	String inLight = "I am at the light";
	
	public MSG(int sourceID,int timeOfMSG) {				//builder
		this.MSGid = numOfMSGS;
		this.timeOfMSG = timeOfMSG;
		this.senderID = sourceID;
		numOfMSGS++;
	}
	
	@Override
	public String toString() {           //Text format
		return "MSG id: " + MSGid + " time of MSG: " + timeOfMSG +" sender ID: " + senderID + " MSG : " + MSG;
	}
	
	public void askLocation(Robot r){
		MSG = askLocation;
	}
	public void giveLightLocation (Robot r){
		MSG = inLight+ r.RobotLocation();
	}
	public void putLocation (Robot r){
		MSG = r.RobotLocation();
	}
	
}
