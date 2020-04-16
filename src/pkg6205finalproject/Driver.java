
public class Driver {
	private boolean giveWay;//willing or not to give way subjectively
	private int cutInWaitingTime;//start cutting in for x ticks after switch turn signal on
	private double maxSpeed;
	
	public Driver() {}
	
	public Driver(boolean giveWay, int cutInWaitingTime, double maxSpeed) {
		this.setGiveWay(giveWay);
		this.setCutInWaitingTime(cutInWaitingTime);
		this.setMaxSpeed(maxSpeed);
	}

	//getters and setters
	public boolean isGiveWay() {
		return giveWay;
	}

	public void setGiveWay(boolean giveWay) {
		this.giveWay = giveWay;
	}

	public int getCutInWaitingTime() {
		return cutInWaitingTime;
	}

	public void setCutInWaitingTime(int cutInWaitingTime) {
		this.cutInWaitingTime = cutInWaitingTime;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
}
