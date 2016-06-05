import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;
/*
 * Two types of robot moving and not moving.
 * Each robot have a battery- life time.
 * The robot know his environment(up,down,right and left to him).
 * Each robot can do one action in one time round.
 */
import java.util.Vector;

public class Robot {
	
	int ID,robotTime;
	boolean canMove;
	double Battery;
	boolean Dead = false;
	Point currLocation;
	Point Env[];
	Vector<String> historyMoves;
	Vector <MSG> MSGhistory;
	Vector<Double> robotDist;
	private String robotData;
	Queue <String> actions;
	public Robot(int ID, boolean canMove,Point p) {					//builder

		this.ID = ID;
		this.canMove = canMove;
		this.currLocation = p;
		Battery = 100;

		Env = getEnv(currLocation);
		robotTime=0;
		historyMoves = new Vector<>();
		
		MSGhistory = new Vector<>();
		robotDist = new Vector<>();
		actions = new LinkedList<>();
	}
	
	private Point[] getEnv(Point p) {								//the robot environment
		Point right = new Point((int)(p.getX()+1),(int)(p.getY()));
		Point left = new Point((int)(p.getX()-1),(int)(p.getY()));
		Point up = new Point ((int)(p.getX()),(int)(p.getY()+1));
		Point down = new Point((int)(p.getX()),(int)(p.getY()-1));
		return new Point []{p,right,left,up,down};
	}
	public void moveUP(){
		currLocation = new Point(currLocation.x,currLocation.y+1);
		Env = getEnv(currLocation);
		historyMoves.add("UP,");
	}
	public void moveDown(){
		currLocation = new Point(currLocation.x,currLocation.y-1);
		Env = getEnv(currLocation);
		historyMoves.add("DOWN,");
	}
	public void moveRight(){
		currLocation = new Point(currLocation.x+1,currLocation.y);
		Env = getEnv(currLocation);
		historyMoves.add("RIGHT,");
	}
	public void moveLeft(){
		currLocation = new Point(currLocation.x-1,currLocation.y);
		Env = getEnv(currLocation);
		historyMoves.add("LEFT,");
	}
	
	interface MoveAction {
        void move();
    }

    private MoveAction[] moveActions = new MoveAction[] {					//action list
    	new MoveAction() { public void move() { Stay(); } },
        new MoveAction() { public void move() { moveRight(); } },
        new MoveAction() { public void move() { moveLeft(); } },
        new MoveAction() { public void move() { moveUP(); } },
        new MoveAction() { public void move() { moveDown(); } },
    };

    public void move(int index) {
        moveActions[index].move();
    }
	protected void Stay() {}
	
	public String RobotLocation(){
		return "["+currLocation.x+","+ currLocation.y+"]";
	}
	
	public String getRobotData(){
		robotData = "Robot ID "+ID +",Real Location: "+ currLocation;
		return robotData;
	}
}
