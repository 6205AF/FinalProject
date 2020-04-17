
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Road extends JPanel{
    
    // total 5 lanes are width 250, length 1000
    // lane 1 => py = 0 rectangle start from left to right , top to bottom
    // lane 2 => py = 50 and etc
    public final static int LANE_WIDTH = 50;
    public final static int LANE_LENGTH = 1000;
    private TrafficSimulation trafficSimulation;
    JTextField counterTxt;
    JTextField flowTxt;
    Random random = new Random();
    HashMap<Integer,ArrayList<Vehicle>> vehicles = new HashMap<Integer,ArrayList<Vehicle>>();

    public Road(TrafficSimulation trafficSimulation) {
        super();
        this.trafficSimulation = trafficSimulation;
        this.vehicles = trafficSimulation.vehicles;
        this.counterTxt = new JTextField();
        this.flowTxt = new JTextField();
        this.add(counterTxt);
        this.add(flowTxt);
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,1000,200);//250*1000 rectangle,
        g.fillRect(0,200,600,50);
        g.fillPolygon(new int[]{600,600,660},new int[]{200,250,200},3);
        g.setColor(Color.WHITE);
        for (int a = 50; a <250;a+=50){ // 50*1000 lane, 5 lanes
            for(int b = 0;b<1000; b += 35){
                g.fillRect(b,a,30,2);
            }
        }
        vehicles = trafficSimulation.vehicles;
        //Draw Vehicles
        for (Integer i = 0; i<TrafficSimulation.lanes; i++){
            for (Vehicle v : vehicles.get(i)){
                v.paintMe(v.getX(), v.getY() ,g);
            }
        }
    }
}
