
import java.awt.*;
import java.util.*;

public abstract class Vehicle {
	public double px,py;//current position
	public double v; //current velocity
	public double length;//Vehicle length
	public double width;//Vehicle width
	public double acceleration;//maximum acceleration per tick
	public double deceleration;//maximum deceleration per tick
	enum direction{
		forward,
		left,
		right
		}
	public direction direction;//moving direction
	public Driver driver;
	public int SignalSwitchedDuration = 0;//Time(ticks) after switch on turn signal
	public int lane;//current (or former in move) lane
	
	public boolean hasMoved;//whether has moved in current tick
	public int frontVehicle;//the id of the front vehicle
        public void paintMe(double px, double py, Graphics g){} //template for child
        public int getX(){
            int px = (int) this.px;
            return px;
        }
        
        public int getY(){
            int py = (int) this.py;
            return py;
        }
        
        public void setX(int newx){
        }
        
        public int getSpeed(){
            int v = (int) this.v;
            return v;
        }
        
        
//	public void paintMe(Graphics g){
//		g.setColor(Color.BLUE);
//		int px = (int) this.px;
//		int py = (int) this.py;
//		int length = (int) this.length;
//		int width = (int) this.width;
//		g.fillRect(px,py,length,width);
//	}
}