
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Road extends JPanel{
    final int LANE_WIDTH = 50;
    final int LANE_LENGTH = 1000;
    //ArrayList<Vehicle> vehicles = new ArrayList();
    Vehicle[][] vehicles;

    public Road(Vehicle[][] vehicles) {
        super();
        this.vehicles = vehicles;
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
        
        for (int i=0; i<vehicles.length;i++) {
				for (int j=0; j<vehicles[i].length;j++) {
					vehicles[i][j].paintMe(g);
				}
			}
        
        
    }
}
