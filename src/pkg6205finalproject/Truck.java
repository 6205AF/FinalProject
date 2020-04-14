import java.awt.*;

public class Truck extends Vehicle{
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
	public int lane;//current (or former in move) lane
	
	public boolean hasMoved;//whether has moved in current tick
	
	public Truck() {}
	
	public Truck (double length, double width, double acceleration, double deceleration, int lane) {
		this.length = length;
		this.width = width;
		this.acceleration = acceleration;
		this.deceleration = deceleration;
		this.px = 0;
		this.py = lane * Road.LANE_WIDTH;
		this.direction = direction.forward;
		this.lane = lane;
	}

	@Override
	public void paintMe(double paintX, double paintY, Graphics g){
		g.setColor(Color.BLUE);
		int length = (int) this.length;
		int width = (int) this.width;
		g.fillRect((int)paintX,(int)paintY,length,width);
	}
}
