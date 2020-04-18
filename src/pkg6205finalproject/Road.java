
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/*
 * change line 54 from
 * g.fillRect(0,0,1000,200);
 * to
 * g.fillRect(0,0,1000,250);
 * and comment out line 55,56 to make gui 5 lanes
 */

public class Road extends JPanel{
    
    // total 5 lanes are width 250, length 1000
    // lane 1 => py = 0 rectangle start from left to right , top to bottom
    // lane 2 => py = 50 and etc
    public final static int LANE_WIDTH = 50;
    public final static int LANE_LENGTH = 1000;
    private TrafficSimulation trafficSimulation;
    JTextField counterTxt;
    JTextField flowTxt;
    JLabel counterLable;
    JLabel flowLable;
    Random random = new Random();
    HashMap<Integer,ArrayList<Vehicle>> vehicles = new HashMap<Integer,ArrayList<Vehicle>>();

    public Road(TrafficSimulation trafficSimulation) {
        super();
        this.trafficSimulation = trafficSimulation;
        this.vehicles = trafficSimulation.vehicles;
        this.counterTxt = new JTextField();
        this.counterLable = new JLabel("Counter");
        this.counterLable.setForeground(Color.red);
        this.flowTxt = new JTextField();
        this.flowLable = new JLabel("Flow");
        this.flowLable.setForeground(Color.red);
        this.add(counterLable);
        this.add(counterTxt);
        this.add(flowLable);
        this.add(flowTxt);
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        if (trafficSimulation.situationNumber == 1 || trafficSimulation.situationNumber == 3){
            g.fillRect(0,0,1000,200);//250*1000 rectangle,
            g.fillRect(0,200,600,50);
            g.fillPolygon(new int[]{600,600,660},new int[]{200,250,200},3);
        }

        if (trafficSimulation.situationNumber == 2 || trafficSimulation.situationNumber == 4){
            g.fillRect(0,0,1000,250);
        }

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
