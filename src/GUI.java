import javax.swing.*;
import java.awt.*;


public class GUI {
	JFrame frame;
	JPanel Panel[][];
	Arena Arena;
	Simulator Simulator;
	/**
	 * Create the panel.
	 */
	public GUI() {
		Simulator = new Simulator("test.txt");
		Panel = new JPanel [Simulator.Arena.ArenaSize][Simulator.Arena.ArenaSize];
		frame = new JFrame("Robots Arena");
		frame.setSize(500, 500);
		frame.getContentPane().setLayout(new GridLayout(Simulator.Arena.ArenaSize,Simulator.Arena.ArenaSize));
		
		initPanel();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		

	}
	private void initPanel() {
		for (int i = 0; i < Simulator.Arena.ArenaSize; i++) {
			for (int j = 0; j < Simulator.Arena.ArenaSize; j++) {
				Panel[i][j] = new JPanel();
				
				if(Simulator.Arena.Arena[i][j] == Simulator.Arena.BlackPanel){
					Panel[i][j].setBackground(Color.black);
				}
				else if (Simulator.Arena.Arena[i][j] == Simulator.Arena.GrayPanel){
					Panel[i][j].setBackground(Color.GRAY);
				}
				else Panel[i][j].setBackground(Color.white);
				
				
				frame.getContentPane().add(Panel[i][j]);
			}
		}
		for (int i = 0; i < Simulator.robots.size(); i++) {
			Point p = Simulator.robots.get(i).currLocation;
			if(Simulator.robots.get(i).canMove)Panel[p.x][p.y].setBackground(Color.red);
			else Panel[p.x][p.y].setBackground(Color.green);
		}
		
		
	}
	public static void main(String[] args) {
		GUI g = new GUI();
	}
}
