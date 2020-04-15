
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JPanel;

public class Road extends JPanel{
    
    // total 5 lanes are width 250, length 1000
    // lane 1 => py = 0 rectangle start from left to right , top to bottom
    // lane 2 => py = 50 and etc
    public final static int LANE_WIDTH = 50;
    public final static int LANE_LENGTH = 1000;
    private TrafficSimulation trafficSimulation;
    Random random = new Random();
    HashMap<Integer,ArrayList<Vehicle>> vehicles = new HashMap<Integer,ArrayList<Vehicle>>();

    public Road(TrafficSimulation trafficSimulation) {
        super();
        this.trafficSimulation = trafficSimulation;
        this.vehicles = trafficSimulation.vehicles;
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,1000,250);//250*1000 rectangle,
        g.setColor(Color.WHITE);
        for (int a = 50; a <250;a+=50){ // 50*1000 lane, 5 lanes
            for(int b = 0;b<1000; b += 35){
                g.fillRect(b,a,30,2);
            }
        }
        //Draw Vehicles
        for (Integer i = 0; i<TrafficSimulation.lanes; i++){
            for (Vehicle v : vehicles.get(i)){
                v.paintMe(v.px, v.py ,g);
            }
        }
    }
}
