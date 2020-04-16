import java.awt.*;

public class Truck extends Vehicle{
	public int SignalSwitchedDuration = 0;//Time(ticks) after switch on turn signal
	
	public Truck() {}
	
	public Truck (double length, double width, double acceleration, double deceleration,int lane) {
		super(length,width,acceleration, deceleration, lane);
		this.setLength(length);
		this.setWidth(width);
		this.setAcceleration(acceleration);
		this.setDeceleration(deceleration);
		this.setPx(0);
		this.setPy((0.1+lane)*Road.WIDTH);
		this.setDirection("forward");
	}

	@Override
	public void paintMe(double paintX, double paintY, Graphics g){
		g.setColor(Color.BLUE);
		int length = (int)this.getLength();
		int width = (int)this.getWidth();
		g.fillRect((int)paintX,(int)paintY,length,width);
	}
}
