
import java.awt.Color;
import java.awt.Graphics;


public class Car extends Vehicle{
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
	public Driver driver;//driver
	public int SignalSwitchedDuration = 0;//Time(ticks) after switch on turn signal
	
	public boolean hasMoved;//whether has moved in current tick
	
	public Car() {}

	// initialize
	public Car (double length, double width, double acceleration, double deceleration, int lane) {
		this.length = length;
		this.width = width;
		this.acceleration = acceleration;
		this.deceleration = deceleration;
		this.px = 0;
		this.py = lane * Road.LANE_WIDTH;
		this.direction = direction.forward;

	}

	public void paintMe(Graphics g){
		g.setColor(Color.BLUE);
		int px = (int) this.px;
		int py = (int) this.py;
		int length = (int) this.length;
		int width = (int) this.width;
		g.fillRect(px,py,length,width);
	}
        

}
