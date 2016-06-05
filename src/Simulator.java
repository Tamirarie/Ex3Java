import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;
/*
 * The simulator controls the life time of the game and connect between all the classes
 */

import javax.swing.plaf.basic.BasicSliderUI.ActionScroller;

public class Simulator {
	Arena Arena;
	Air Air;
	Vector<Robot> robots = new Vector<>();
	Log log;
//	Vector<String> params;
	int time = 24*60*60*1000;
	Point found[];
	public Simulator(String FileParamaters) {				//builder

		int Robots[] = ReadCSV(FileParamaters);
		System.out.println(Arrays.toString(Robots));
		Arena = new Arena(100);
		robots = new Vector<>();
		Air = new Air();
		log = new Log("ArenaLog");
		createRobots(Robots);
		found=new Point[robots.size()];

	}



	public void Action(Robot r,int currTime) {
		if(r.Battery==0){
			r.Dead = true;
			log.addSentence("Robot "+ r.ID + " Just Died!") ; 
			return ;
		}

		if(Arena.Arena[r.currLocation.x][r.currLocation.y] == Arena.WhitePanel){ // robot is in sun
			if(r.Battery<100) r.Battery+= 0.00001;
		}
		else r.Battery-= 0.00001;


		Vector<MSG> unRead = msgRead(r);
		if(!r.canMove){
			if(unRead.size() != 0) { /// if robot didnt read message
				msgInRange(r,unRead);
			}
			else{
				giveLocation(r,currTime);
			}
		}
		else{
			if(unRead.size() != 0) {
				msgInRange(r, unRead);
			}
			else if(Arena.Arena[r.currLocation.x][r.currLocation.y] == Arena.WhitePanel)
			{
				askLocation(r,currTime);
				return;
			}
		//	else checkEnv(r);
		}
	}

	private void giveLocation(Robot r,int currTime) {
		MSG m = new MSG(r.ID,currTime);
		m.putLocation(r);
		log.addSentence("Robot " + r.ID + " Posted MSG: " + m);
		Air.addMSG(m);
	}



	private void askLocation(Robot r,int currTime) {
		MSG m = new MSG(r.ID , currTime);
		m.askLocation(r);
		log.addSentence("Robot " + r.ID + " Posted MSG: " + m);
		Air.addMSG(m);
	}



	private void msgInRange(Robot reciver,Vector<MSG> unRead) {

		
		Iterator<MSG> iter = unRead.iterator();
		while (iter.hasNext()) {
			MSG msg = iter.next();
			double dist = getDistMSG(reciver,msg);
	//		System.out.println(dist);
			if(dist < 250) {
				readMSG(msg,reciver);
				return;
			}
			else if(dist > 500){
				iter.remove();
			}
		//	if(unRead.size() == 0) break;
		}



	}

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
				System.out.println("3 messages readed");
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
	

	private int countMsgHistoryLocations(Robot r) {
		int counter = 0;
		for (int i = 0; i < r.MSGhistory.size(); i++) {
			if(r.MSGhistory.get(i).MSG.contains("[")) counter++;
		}
		return counter;
	}



	private double getDistMSG(Robot reciver, MSG msg) {
		Point sender = robots.get(msg.senderID).currLocation;
//		System.out.println("Sender robot "+sender);
//		System.out.println("Reciver robot "+reciver.currLocation);
		double dist = reciver.currLocation.distance(sender);
		
		return dist;
	}



	private Vector<MSG> msgRead(Robot r){
		Vector<MSG> unRead = new Vector<>();
		boolean read = false;
		for (int i = 0; i < Air.messages.size(); i++) { // check if there is messages near by in air
			read = false;
			for (int j = 0; j < r.MSGhistory.size(); j++) {
				if(r.MSGhistory.get(j).MSGid == Air.messages.get(i).MSGid){ // if robot read the message
					read = true;
				}
			}
			if(read == false) unRead.add(Air.messages.get(i));
		}
	//	System.out.println(unRead);
		return unRead;
	}

	private void checkEnv(Robot r) {
		int a = (int)(Math.random()*4+1);
		for (int i = 0; i < 3; i++) {
			a = (a+i)%4;
			if(Arena.Arena[r.Env[a].x][r.Env[a].y] != Arena.BlackPanel)
			{
				r.move(a);
				if(a!= 0)log.addSentence("Robot "+ r.ID + " moved from "+ r.RobotLocation()+" : " + r.historyMoves);
				break;}
		}

	}


	private void createRobots(int[] robots) {
		int counter = 0;
		while(counter < robots[0]){
			Point p = new Point();
			do{
				p = new Point((int)(Math.random()*Arena.ArenaSize),(int)(Math.random()*Arena.ArenaSize));
			}while(Arena.Arena[p.x][p.y] == Arena.BlackPanel);
			Robot r = new Robot(counter++, true, p);
			this.robots.add(r);
		}
		int numOfRobots = counter;
		counter = 0;
		
		while(counter < robots[1]){
			Point p = new Point();
			do{
				p = new Point((int)(Math.random()*Arena.ArenaSize),(int)(Math.random()*Arena.ArenaSize));
			}while(Arena.Arena[p.x][p.y] == Arena.BlackPanel);
			Robot r = new Robot(numOfRobots++, false, p);
			this.robots.add(r);
			counter++;
		}


	}
	private int [] ReadCSV(String File) {
		Scanner scanner;
		int [] RobotsType = new int [2];
		try {
			scanner = new Scanner(new File(File));
			int i = 0;
			while(scanner.hasNextInt()){
				RobotsType[i++] = scanner.nextInt();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return RobotsType;

	}
	void FoundedRobot(Point p,int index){
		if(found[index]==null){
		found[index]=p;	
		}
		else{
			found[index].x=(found[index].x+p.x)/2;
		    found[index].y=(found[index].y+p.y)/2;
		   
		}
	}

	public static void main(String[] args) {
		Simulator s = new Simulator("test.txt");
	}
}
