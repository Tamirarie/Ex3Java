import java.util.Vector;

public class Air {
	Vector<MSG> messages;
	public Air() {
		messages = new Vector<>();
	}
	void addMSG(MSG m){
		messages.add(m);
	}
	
}
