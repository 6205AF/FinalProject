
import java.awt.*;
import java.util.*;

public abstract class Vehicle {
	private double px,py;//current position
	private double v; //current velocity
	private double length;//Vehicle length
	private double width;//Vehicle width
	private double acceleration;//maximum acceleration per tick
	private double deceleration;//maximum deceleration per tick
	private String direction;//moving direction
	private Driver driver;
	private int SignalSwitchedDuration = 0;//Time(ticks) after switch on turn signal
	private Vehicle nextVehicle;//next Vehicle waiting for spawning

	private boolean hasMoved;//whether has moved in current tick
	
	public Vehicle() {}
	public Vehicle(double length, double width, double acceleration, double deceleration, int lane) {
		this.setLength(length);
		this.setWidth(width);
		this.setAcceleration(acceleration);
		this.setDeceleration(deceleration);
		this.setPx(0);
		this.setPy((0.1+lane)*Road.LANE_WIDTH);
		this.setDirection("forward");
	}
	
	public void print() {
		System.out.println("px:" + this.getPx());
		System.out.println("py:" + this.getPy());
		System.out.println("v:" + this.getV());
		System.out.println("length:" + this.getLength());
		System.out.println("acceleration:" + this.getAcceleration());
		System.out.println("direction:" + this.getDirection());
		System.out.println("hasMoved:" + this.isHasMoved());
	}
	
	public void paintMe(double px, double py, Graphics g){} //template for child
	
	public int getX(){
		int px = (int)this.getPx();
		return px;
	}
	
    public int getY(){
        int py = (int)this.getPy();
        return py;
    }

	//	public void paintMe(Graphics g){
	//		g.setColor(Color.BLUE);
	//		int px = (int) this.px;
	//		int py = (int) this.py;
	//		int length = (int) this.length;
	//		int width = (int) this.width;
	//		g.fillRect(px,py,length,width);
	//	}
	
	//getters and setters
	public int getSpeed(){
		int v = (int) this.v;
		return v;
	}
	public double getPx() {
		return px;
	}
	public void setPx(double px) {
		this.px = px;
	}
	public double getPy() {
		return py;
	}
	public void setPy(double py) {
		this.py = py;
	}
	public double getV() {
		return v;
	}
	public void setV(double v) {
		this.v = v;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getAcceleration() {
		return acceleration;
	}
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}
	public double getDeceleration() {
		return deceleration;
	}
	public void setDeceleration(double deceleration) {
		this.deceleration = deceleration;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public Driver getDriver() {
		return driver;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	public int getSignalSwitchedDuration() {
		return SignalSwitchedDuration;
	}
	public void setSignalSwitchedDuration(int signalSwitchedDuration) {
		SignalSwitchedDuration = signalSwitchedDuration;
	}
	public boolean isHasMoved() {
		return hasMoved;
	}
	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}
}