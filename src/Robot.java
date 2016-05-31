import java.awt.Point;
import java.util.Vector;

public class Robot {
	int ID,robotTime;;
	boolean canMove;
	static int Battery;
	
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
		
	}
}
