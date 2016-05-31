import java.awt.Point;

public class Robot {
	int ID;
	boolean canMove;
	Point currLocation;
	public Robot(int ID, boolean canMove,Point p) {
		this.ID = ID;
		this.canMove = canMove;
		this.currLocation = p;
	}
}
