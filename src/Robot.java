import java.awt.Point;
import java.util.Vector;

public class Robot {
	int ID,robotTime;
	boolean canMove;
	double Battery;
	
	Point currLocation;
	Point Env[];
	Vector<String> historyMoves,RobotData,MSGhistory;
	Vector<Double> robotDist;
	MSG Recived;
		    
	public Robot(int ID, boolean canMove,Point p) {
		
		this.ID = ID;
		this.canMove = canMove;
		this.currLocation = p;
		Battery = 100;
		
		Point right = new Point((int)p.getX()+1,(int)p.getY());
		Point left = new Point((int)p.getX()-1,(int)p.getY());
		Point up = new Point ((int)p.getX(),(int)p.getY()+1);
		Point down = new Point((int)p.getX(),(int)p.getY()-1);
		
		Env = new Point[]{currLocation,right,left,up,down};
		robotTime=0;
		historyMoves = new Vector<>();
		RobotData = new Vector<>();
		MSGhistory = new Vector<>();
		robotDist = new Vector<>();
		
	}
	
}
