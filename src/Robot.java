import java.awt.Point;
import java.util.Vector;

public class Robot {
	int ID,robotTime;
	boolean canMove;
	double Battery;
	boolean Dead = false;
	Point currLocation;
	Point Env[];
	Vector<String> historyMoves,RobotData;
	Vector <MSG> MSGhistory;
	Vector<Double> robotDist;
	MSG Recived;
	public Robot(int ID, boolean canMove,Point p) {

		this.ID = ID;
		this.canMove = canMove;
		this.currLocation = p;
		Battery = 100;

		Env = getEnv(currLocation);
		robotTime=0;
		historyMoves = new Vector<>();
		RobotData = new Vector<>();
		MSGhistory = new Vector<>();
		robotDist = new Vector<>();

	}
	private Point[] getEnv(Point p) {
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

    private MoveAction[] moveActions = new MoveAction[] {
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
}
