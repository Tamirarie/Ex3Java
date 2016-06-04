import java.util.Vector;

public class Air {                 	    
	Vector<MSG> messages;				//vector used for storing messages
	public Air() {						////builder
		messages = new Vector<>();
	}
	void addMSG(MSG m){
		messages.add(m);
	}
	
}
