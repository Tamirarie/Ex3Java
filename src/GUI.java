import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;


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

		frame.setSize(500, 500);                                   //size of view
		frame.getContentPane().setLayout(new GridLayout(Simulator.Arena.ArenaSize,Simulator.Arena.ArenaSize));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					Simulator.writer.flush();
					Simulator.writer.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		StartSim();


	}
	public void StartSim(){                       //connecting between the GUI to the Simulator
		initPanel();
		Vector <Robot> temp = getNewRobots(Simulator.robots) ;
		for (int i = 0; i < Simulator.time; i++) {
			Collections.shuffle(temp);
			for (Robot r : temp) {
				if(!r.Dead)
					Simulator.Action(r,i);
			}

			/////the accuracy of the locations not fully correct, for now we will accept this...
			if(foundAllLocations()){
				Simulator.log.addSentence("============================================");
				Simulator.log.addSentence("All robots found their locations! finishing");
				Simulator.log.addSentence(Arrays.toString(Simulator.found));
				try {
					Simulator.writer.flush();
					Simulator.writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	    
				System.exit(0);
			}
			if(i%1 == 0){updatePanel();
			frame.getContentPane().update(frame.getGraphics());
			}
		}
	}

	private boolean foundAllLocations(){
	/////the accuracy of the locations not fully correct, for now we will accept this...
		for (int i = 0; i < Simulator.found.length; i++) {
			if(Simulator.found[i] == null) return false;
//			if(!diffPoints(i)) return false;
		}
		return true;
	}

	private boolean diffPoints(int i) {
		return(Math.abs(Simulator.found[i].x - Simulator.robots.get(i).currLocation.x) <= 1
				&& Math.abs(Simulator.found[i].y - Simulator.robots.get(i).currLocation.y)<= 1);

	}
	private Vector<Robot> getNewRobots(Vector <Robot> vec){
		Vector<Robot> ans = new Vector<>();
		for (int i = 0; i < vec.size(); i++) {
			ans.add(vec.get(i));
		}
		return ans;
	}

	private void updatePanel(){                                               //After one round of action, arena update 
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


	private void initPanel() {												//insert the arena to GUI
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
