import javax.swing.*;
import java.awt.*;


public class GUI {
	JFrame frame;
	JPanel Panel[][];
	Simulator Simulator;
	/**
	 * Create the panel.
	 */
	public GUI() {
		Simulator = new Simulator("test.txt");
		frame = new JFrame("Robots Arena");
	
		frame.setSize(500, 500);
		frame.getContentPane().setLayout(new GridLayout(Simulator.Arena.ArenaSize,Simulator.Arena.ArenaSize));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		StartSim();


	}
	public void StartSim(){
		initPanel();
		for (int i = 0; i < 100; i++) {
			for (Robot r : Simulator.robots) {
				if(!r.Dead)
				Simulator.Action(r);
			}

			updatePanel();
			frame.getContentPane().update(frame.getGraphics());
		//	initPanel();
		}
	}

	private void updatePanel(){
		for (int i = 0; i < Simulator.Arena.ArenaSize; i++) {
			for (int j = 0; j < Simulator.Arena.ArenaSize; j++) {
				
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
	 frame.repaint();
	}


	private void initPanel() {
		Panel = new JPanel [Simulator.Arena.ArenaSize][Simulator.Arena.ArenaSize];
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
		frame.setVisible(true);

	}
	public static void main(String[] args) {
		GUI g = new GUI();
	}
}
