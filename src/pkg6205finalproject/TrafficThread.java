import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TrafficThread extends Thread{

	private JFrame jFrame;
	public static int refresh = 500; // refresh time unit
	public static int refreshTimes = 20;
	private TrafficSimulation trafficSimulation;

	public JFrame getjFrame() {
		return jFrame;
	}

	public void setjFrame(JFrame jFrame) {
		this.jFrame = jFrame;
	}

	public TrafficThread(){}

	public TrafficThread(JFrame jFrame, TrafficSimulation trafficSimulation){
		this.jFrame = jFrame;
		this.trafficSimulation = trafficSimulation;
	}

	@Override
	public void run() {
		//run the simulation with Move method
		HashMap<Integer,ArrayList<Vehicle>> vehicles = trafficSimulation.vehicles;
		Move move = new Move();
		while (true){
			//Draw the road situation
			JPanel jPanel = trafficSimulation.road;
			jPanel.repaint();
			jFrame.add(jPanel,BorderLayout.CENTER);
			jFrame.setVisible(true);
			// move for next tick
			move.move(this.trafficSimulation);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
