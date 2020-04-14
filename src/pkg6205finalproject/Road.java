
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JPanel;

public class Road extends JPanel{
    
    // total 5 lanes are width 250, length 1000
    // lane 1 => py = 0 rectanle start from left to right , top to bottom
    // lane 2 => py = 50 and etc
    public final static int LANE_WIDTH = 50;
    public final static int LANE_LENGTH = 1000;
    Random random = new Random();
    HashMap<Integer,ArrayList<Vehicle>> vehicles = new HashMap<Integer,ArrayList<Vehicle>>();

    public Road(HashMap<Integer,ArrayList<Vehicle>> vehicles) {
        super();
        this.vehicles = vehicles;
    }
    public void addVehicle(Vehicle v){
        int lane = random.nextInt(5);
        vehicles.get(lane).add(v);
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
        for (int i = 0;i<5;i++){
            for (Vehicle v : vehicles.get(i)){
                v.paintMe(g);
            }
        }
        
        
    }
    
    public void step(){
        for (int i = 0;i<5;i++){
            for (Vehicle v : vehicles.get(i)){
                v.setX(v.getX()+v.getSpeed());
            }
        }
    }
}
