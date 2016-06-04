import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
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
	Vector<String> params;
	int time = 24*60*60*1000;
	public Simulator(String FileParamaters) {				//builder

		int Robots[] = ReadCSV(FileParamaters);
		System.out.println(Arrays.toString(Robots));
		Arena = new Arena(100);
		robots = new Vector<>();
		Air = new Air();
		log = new Log("ArenaLog");
		createRobots(Robots);


	}



	public void Action(Robot r) {
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
				giveLocation(r);
			}
		}
		else{
			if(Arena.Arena[r.currLocation.x][r.currLocation.y] == Arena.WhitePanel)
			{
				askLocation(r);
				return;
			}
			checkEnv(r);
		}
	}

	private void giveLocation(Robot r) {
		MSG m = new MSG(r.ID,time);
		m.putLocation(r);
		log.addSentence("Robot " + r.ID + " Posted MSG: " + m);
		Air.addMSG(m);
	}



	private void askLocation(Robot r) {
		MSG m = new MSG(r.ID , time);
		m.askLocation(r);
		log.addSentence("Robot " + r.ID + " Posted MSG: " + m);
		Air.addMSG(m);
	}



	private void msgInRange(Robot reciver,Vector<MSG> unRead) {

		for (MSG msg : unRead) {
			double dist = getDistMSG(reciver,msg);
			if(dist < 250) {
				readMSG(msg,reciver);
				return;
			}
			else if(dist > 500){
				unRead.remove(msg);
			}
		}



	}

	private void readMSG(MSG msg,Robot r) {
		r.MSGhistory.add(msg);
		log.addSentence("Robot "+ r.ID + "Read : " + msg.toString());
		/*
		 * need to check in next turn what to do with the message
		 */

	}



	private double getDistMSG(Robot reciver, MSG msg) {
		Point sender = robots.get(msg.senderID).currLocation;
		double dist = reciver.currLocation.distanceSq(sender);
		return dist;
	}



	private Vector<MSG> msgRead(Robot r){
		Vector<MSG> unRead = new Vector<>();
		boolean read = false;
		for (int i = 0; i < Air.messages.size(); i++) { // check if there is messages near by in air
			read = false;
			for (int j = 0; j < r.MSGhistory.size(); j++) {
				if(r.MSGhistory.get(i).MSGid == Air.messages.get(i).MSGid){ // if robot read the message
					read = true;
				}
				if(read == false) unRead.add(Air.messages.get(i));
			}
		}
		return unRead;
	}

	private void checkEnv(Robot r) {
		int a = (int)(Math.random()*4+1);
		for (int i = 0; i < 3; i++) {
			a = (a+i)%4;
			if(Arena.Arena[r.Env[a].x][r.Env[a].y] != Arena.BlackPanel)
			{
				r.move(a);
				if(a!= 0)log.addSentence("Robot "+ r.ID + " moved from "+ r.currLocation+": " + r.historyMoves);
				break;}
		}

	}



	public double getSignal(Robot r1, Robot r2){
		double dist = r1.currLocation.x*r2.currLocation.x + r1.currLocation.y*r2.currLocation.y;
		double sq = Math.sqrt(dist);
		return sq;
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
		counter = 0;
		while(counter < robots[1]){
			Point p = new Point();
			do{
				p = new Point((int)(Math.random()*Arena.ArenaSize),(int)(Math.random()*Arena.ArenaSize));
			}while(Arena.Arena[p.x][p.y] == Arena.BlackPanel);
			Robot r = new Robot(counter++, false, p);
			this.robots.add(r);
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

	public static void main(String[] args) {
		Simulator s = new Simulator("test.txt");
	}
}
