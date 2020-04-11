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
	
	public boolean hasMoved;//whether has moved in current tick
	public int frontVehicle;//the id of the front vehicle

}