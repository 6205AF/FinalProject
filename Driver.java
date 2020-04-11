
public class Driver {
	public boolean giveWay;//willing or not to give way subjectively
	public int cutInWaitingTime;//start cutting in for x ticks after switch turn signal on
	public double maxSpeed;
	
	public Driver() {}
	
	public Driver(boolean giveWay, int cutInWaitingTime, double maxSpeed) {
		this.giveWay = giveWay;
		this.cutInWaitingTime = cutInWaitingTime;
		this.maxSpeed = maxSpeed;
	}
	
	public void controlVehicle() {
		
	}
	
	public void judge() {
		
	}
}
