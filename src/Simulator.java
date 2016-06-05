import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;
/*
 * The simulator controls the life time of the game and connect between all the classes
 */



public class Simulator {
	Arena Arena;
	Air Air;
	Vector<Robot> robots = new Vector<>();
	Log log;
	int time = 24*60*60*1000;
	Point found[];
	int staticRobots,DynamicRobots;
	public Simulator(String FileParamaters) {				//builder

	//	int Robots[] = ReadCSV(FileParamaters);
		readFile("test.txt");
	//	System.out.println(Arrays.toString(Robots));
		//Arena = new Arena(100);
		robots = new Vector<>();
		Air = new Air();
		log = new Log("ArenaLog");
		found = new Point[staticRobots+DynamicRobots];
		createRobots();

	}



	/*
	 * method action that presents an action of certain robot in simulator
	 */
	public void Action(Robot r,int currTime) {
		updateBattery(r);
		Vector<MSG> unRead = msgRead(r);
		
		if(!r.canMove){ /// if robot cant move
			if(unRead.size() != 0) { /// if robot didnt read message
				msgInRange(r,unRead);
			}
			else{
				if(!r.actions.isEmpty()){ /// if robot has action that he wanted to do in previous action
					String action = r.actions.poll();
					if(action == "SendLocation")giveLocation(r,currTime);
					else if (action == "SendLight") giveInLight(r, currTime);
				}

			}
		}
		else{ /// robot can move
			if(unRead.size() != 0) {
				msgInRange(r, unRead);
			}
			else if(Arena.Arena[r.currLocation.x][r.currLocation.y] == Arena.WhitePanel)
			{
				askLocation(r,currTime);
				return;
			}
					else checkEnv(r);
		}
	}
	private void updateBattery(Robot r) {
		if(r.Battery==0){
			r.Dead = true;
			log.addSentence("Robot "+ r.ID + " Just Died!") ; 
			return ;
		}

		if(Arena.Arena[r.currLocation.x][r.currLocation.y] == Arena.WhitePanel){ // robot is in sun
			if(r.Battery<100) r.Battery+= 0.00001;
		}
		else r.Battery-= 0.00001;

	}



	/*
	 * method that posting give location message to air
	 */
	private void giveLocation(Robot r,int currTime) {
		MSG m = new MSG(r.ID,currTime);
		m.putLocation(r);
		log.addSentence("Robot " + r.ID + " Posted MSG: " + m);
		Air.addMSG(m);
	}


	/*
	 * method that posting ask location message to air
	 */

	private void askLocation(Robot r,int currTime) {
		MSG m = new MSG(r.ID , currTime);
		m.askLocation(r);
		log.addSentence("Robot " + r.ID + " Posted MSG: " + m);
		Air.addMSG(m);
	}

	private void giveInLight(Robot r, int currTime){
		MSG m = new MSG(r.ID,currTime);
		m.giveLightLocation(r);
		log.addSentence("Robot " + r.ID + " Posted MSG: " + m);
		Air.addMSG(m);
	}

	/*
	 * check which messages are in range and selecting the first between them to read it
	 */
	private void msgInRange(Robot reciver,Vector<MSG> unRead) {
		Iterator<MSG> iter = unRead.iterator();
		while (iter.hasNext()) {
			MSG msg = iter.next();
			double dist = getDistMSG(reciver,msg);
			System.out.println(dist);
			if(dist < 20) {
			//	if(reciver.canMove && msg.MSG == msg.askLocation){
			//		iter.remove();
			//	}
			//	else {
				readMSG(msg,reciver);
				return;
		//		}
			}
			else if(dist > 50){
				iter.remove();
			}
			//	if(unRead.size() == 0) break;
		}



	}

	/*
	 * method that presents robot that reads message and deciding what to do with it
	 */
	private void readMSG(MSG msg,Robot r) {
		r.MSGhistory.add(msg);
		log.addSentence("Robot "+ r.ID + " Read : " + msg.toString());

		if(!r.canMove){ ///Robot cant move
			if(msg.MSG == msg.askLocation) r.actions.add("SendLocation");
			else if(msg.MSG == msg.outOfBattery ){
				for (int i = 0; i < r.Env.length; i++) {
					if(Arena.Arena[r.Env[i].x][r.Env[i].y] == Arena.WhitePanel){
						r.actions.add("SendLight");
						break;
					}
				}
			}
		}
		else{ ////Robot can move
			if(countMsgHistoryLocations(r) == 3){
				//			System.out.println("3 messages readed");
				Point[] points = createPoints(r);
				double x = rmsX(points,r);
				double y = rmsY(points,r);
				Point robotLocation = new Point((int)x,(int) y);
				FoundedRobot(robotLocation, r.ID);
				log.addSentence("Robot " + r.ID +" found his location! : " +  robotLocation);
				log.addSentence("Robot Data : " + r.getRobotData());
			}
		}


	}
	/*
	 * method that create array of 3 points from the robot msg history
	 * after we know that the robot has 3 messages of location points
	 */
	private Point[] createPoints(Robot r) {
		Point[] points = new Point[3];
		int counter=0;
		for (int i = 0; i < r.MSGhistory.size() ; i++) {
			String temp = r.MSGhistory.get(i).MSG;
			if(temp.contains("[")){ /// if msg content is point
				int find = temp.indexOf(",");
				int x = Integer.valueOf(temp.substring(1, find));
				int y = Integer.valueOf(temp.substring(find+1,temp.length()-1));
				points[counter++] = new Point (x,y);
			}
			if(counter == 3) break; /// we have 3 points
		}
		return points;
	}

	/*
	 * method that counts if certain robot has in msg history
	 * at least 3 messages that contain information about locations
	 */
	private int countMsgHistoryLocations(Robot r) {
		int counter = 0;
		for (int i = 0; i < r.MSGhistory.size(); i++) {
			if(r.MSGhistory.get(i).MSG.contains("[")) counter++;
		}
		return counter;
	}


	/*
	 * method that calculates the distance between the sender robot and the reciver robot
	 */
	private double getDistMSG(Robot reciver, MSG msg) {

		Point sender = robots.get(msg.senderID).currLocation;
		double dist = reciver.currLocation.distance(sender);

		return dist;
	}


	/*
	 * method that returns vector of unread messages that are in the air
	 */
	private Vector<MSG> msgRead(Robot r){
		Vector<MSG> unRead = new Vector<>();
		boolean read = false;
		for (int i = 0; i < Air.messages.size(); i++) { // check if there is messages near by in air
			read = false;
			for (int j = 0; j < r.MSGhistory.size(); j++) {
				if(r.MSGhistory.get(j).MSGid == Air.messages.get(i).MSGid
						&& r.MSGhistory.get(j).MSG == Air.messages.get(i).MSG){ // if robot read the message
					read = true;
				}
			}
			if(read == false) unRead.add(Air.messages.get(i));
		}

		return unRead;
	}

	/*
	 * Enviroment algorithm that tells the robot in which direction to move
	 * currently the algorithm tells the robot to move randomly if the panel isnt black
	 */
	private void checkEnv(Robot r) {
		int a = (int)(Math.random()*4+1);
		for (int i = 0; i < 3; i++) {
			a = (a+i)%4;
			Point temp = new Point(r.Env[a].x, r.Env[a].y);
			if(Arena.Arena[r.Env[a].x][r.Env[a].y] != Arena.BlackPanel) /// another robot on desired panel
			{
				r.move(a);
				if(found[r.ID] != null){ /// we want to update the founded location
					Point p = found[r.ID];
					if(a == 1) FoundedRobot(new Point(p.x+1, p.y), r.ID); ///moved right
					else if(a == 2) FoundedRobot(new Point(p.x-1, p.y), r.ID); // moved left
					else if(a == 3) FoundedRobot(new Point(p.x, p.y+1), r.ID); // moved up
					else if(a == 4) FoundedRobot(new Point(p.x, p.y-1), r.ID); // moved down
				}
				if(a!= 0)log.addSentence("Robot "+ r.ID + " moved from "+ r.RobotLocation()+" : " + r.historyMoves);
				break;}
		}

	}


	/*
	 * RMS algorithem to find location point of robot by
	 * 3 points given by 3 read messages
	 */
	public static double rmsX(Point[] points,Robot r){
		double ms = 0;
		for (int i = 0; i < points.length; i++) {
			ms += points[i].x * r.currLocation.x;
		}
		ms /= points.length;
		return Math.sqrt(ms);
	}
	public static double rmsY(Point[] points,Robot r){
		double ms = 0;
		for (int i = 0; i < points.length; i++) {
			ms += points[i].y * r.currLocation.y;
		}
		ms /= points.length;
		return Math.sqrt(ms);
	}

	/////////////////////////////////////////////////

	private boolean robotOnPanel(Point p){
		for (int i = 0; i < robots.size(); i++) {
			if(robots.get(i).currLocation == p) return true;
		}
		return false;
	}

	/*
	 * Create robots from array that presents two types of robots
	 * robots that can move and robots that cant move
	 */
	private void createRobots() {
		int counter = 0;
		while(counter < DynamicRobots){ ///
			Point p = new Point();
			do{
				p = new Point((int)(Math.random()*Arena.ArenaSize),(int)(Math.random()*Arena.ArenaSize));
			}while(Arena.Arena[p.x][p.y] == Arena.BlackPanel);
			Robot r = new Robot(counter++, true, p);
			this.robots.add(r);

		}
		int numOfRobots = counter;
		counter = 0;

		while(counter < staticRobots){
			Point p = new Point();
			do{
				p = new Point((int)(Math.random()*Arena.ArenaSize),(int)(Math.random()*Arena.ArenaSize));
			}while(Arena.Arena[p.x][p.y] == Arena.BlackPanel);
			Robot r = new Robot(numOfRobots++, false, p);
			this.robots.add(r);
			FoundedRobot(p, r.ID);
			counter++;
		}


	}

	/*
	 * function to read csv file that has two numbers that represent the num
	 * of two robots : can move and that cant move
	 */
	private void readFile (String fileName){
		
		Scanner read;
		try {
			read = new Scanner(new File(fileName));
			while (read.hasNextLine()) {
				
			      String line = read.nextLine();
			      String Static[] = line.split("Static Robots");
			      String Dynamic[] = line.split("Dynamic Robots");
			      String ArenaSize[] = line.split("ArenaSize");
			      String FillArenaBlack[] = line.split("FillArenaBlack");
			      String randomWhites[] = line.split("randomNumOfWhitePanels");
			      
			      if(Static.length > 1) staticRobots = Integer.parseInt(Static[1].trim());   //System.out.println("Value as Long: " + Long.parseLong(Static[1].trim()));
			      if(Dynamic.length > 1)DynamicRobots = Integer.parseInt(Dynamic[1].trim()); //System.out.println("Value as Long: " + Long.parseLong(Dynamic[1].trim()));
			      if(ArenaSize.length > 1)Arena = new Arena(Integer.parseInt(ArenaSize[1].trim()));  //System.out.println("Value as Long: " + Long.parseLong(ArenaSize[1].trim()));
			      if(FillArenaBlack.length > 1){
			    	  String values[] = FillArenaBlack[1].split(",");
			    	  Arena.fillArenaBlack(Integer.parseInt(values[0].trim()), Integer.parseInt(values[1].trim()), Integer.parseInt(values[2].trim()), Integer.parseInt(values[3].trim())); // System.out.println(Arrays.toString(FillArenaBlack));
			      }
			      if(randomWhites.length > 1){
			    	  String values[] = randomWhites[1].split(",");
			    	  Arena.fillArenaWhite(Integer.parseInt(values[0].trim()), Integer.parseInt(values[1].trim()), Integer.parseInt(values[2].trim()), Integer.parseInt(values[3].trim()));
			      }
			    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	void FoundedRobot(Point p,int index){

		if(found[index]==null){
			found[index]=p;	
		}
		else{
			found[index].x=(found[index].x+p.x)/2;
			found[index].y=(found[index].y+p.y)/2;
		
		}
		//if(diffPoints(index))
			robots.get(index).canMove = false;
	}
	private boolean diffPoints(int i) {
		return(Math.abs(found[i].x - robots.get(i).currLocation.x) <= 1
				&& Math.abs(found[i].y - robots.get(i).currLocation.y)<= 1);
		
	}
	public static void main(String[] args) {
		//		Simulator s = new Simulator("test.txt");
	//	readFile("test.txt");
	}
}
