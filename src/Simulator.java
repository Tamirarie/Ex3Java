import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

public class Simulator {
	Arena Arena;
	Air Air;
	Vector<Robot> robots = new Vector<>();
	Log log;
	Vector<String> params;
	int time = 24*60*60*1000;
	public Simulator(String FileParamaters) {

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
		

		int indexOfMsg = msgRead(r);
		if(!r.canMove){
			if(indexOfMsg != -1) { /// if robot didnt read message
			
				msgInRange(r,indexOfMsg);
			}
		}
		else{
			checkEnv(r);
		}
	}
	
	private boolean msgInRange(Robot reciver,int i) {
		
		Point sender = robots.get(Air.messages.get(i).senderID).currLocation;
		double dist = reciver.currLocation.distanceSq(sender);
		
		if(dist < 50) return true;
		if(dist > 500) return false;
		
		Vector<MSG> unRead = new Vector<>();
		unRead.add(Air.messages.get(i));
		
		return true;
		
	}



	private int msgRead(Robot r){
		
		for (int i = 0; i < Air.messages.size(); i++) { // check if there is messages near by in air
			for (int j = 0; j < r.MSGhistory.size(); j++) {
				if(r.MSGhistory.get(i).MSGid == Air.messages.get(i).MSGid){ // if robot read the message
					return i;
				}
			}
		}
		return -1;
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
