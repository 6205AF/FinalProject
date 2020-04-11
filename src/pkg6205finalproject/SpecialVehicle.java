
public class SpecialVehicle extends Vehicle{
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
	
	public SpecialVehicle() {}
	
	public SpecialVehicle (double length, double width, double acceleration, double deceleration) {
		this.length = length;
		this.width = width;
		this.acceleration = acceleration;
		this.deceleration = deceleration;
		this.px = 0;
		this.direction = direction.forward;
	}
}
