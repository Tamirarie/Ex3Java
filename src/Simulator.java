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
	File logFile;
	Vector<String> params;
	int time = 24*60*60*1000;
	public Simulator(String FileParamaters) {
		
		int Robots[] = ReadCSV(FileParamaters);
		System.out.println(Arrays.toString(Robots));
		Arena = new Arena(100);
		robots = new Vector<>();
		Air = new Air();
		createRobots(Robots);
		
		StartSim();

	}
	
	
	public void StartSim(){
		for (int i = 0; i < time; i++) {
			for (Robot r : robots) {
				Action(r);
			}
		}
	}
	public void Action(Robot r) {
		if(Arena.Arena[r.currLocation.x][r.currLocation.y] == Arena.WhitePanel){ // robot is in sun
			if(r.Battery<100) r.Battery+= 0.00001;
		}
		else r.Battery-= 0.00001;

		
		if(!r.canMove){
			for (int i = 0; i < Air.messages.size(); i++) { // check if there is messages near by in air
				double signal = Air.messages.get(i).signal;
			}
		}
		else{
			
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
