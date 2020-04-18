import java.awt.Color;
import java.awt.Graphics;

public class SpecialVehicle extends Vehicle{
	public SpecialVehicle() {}
	
	public SpecialVehicle (double length, double width, double acceleration, double deceleration, int lane) {
		super(length,width,acceleration, deceleration, lane);
		this.setLength(length);
		this.setWidth(width);
		this.setAcceleration(acceleration);
		this.setDeceleration(deceleration);
		this.setPx(0);
		this.setPy((0.1+lane)*Road.WIDTH);
		this.setDirection("forward");
	}
	
	public void paintMe(double paintX, double paintY, Graphics g){
		g.setColor(Color.red);
		int length = (int)this.getLength();
		int width = (int)this.getWidth();
		g.fillRect((int)paintX,(int)paintY,length,width);
		if (getDirection().equals("left")){
			g.setColor(Color.YELLOW);
			g.fillRect((int)paintX,(int)paintY,12,12);
			g.fillRect((int)(paintX+length-12),(int)paintY,12,12);

		}
		if (getDirection().equals("right")){
			g.setColor(Color.YELLOW);
			g.fillRect((int)paintX,(int)(paintY+width-12),12,12);
			g.fillRect((int)(paintX+length-12),(int)(paintY+width-12),12,12);
		}
	}
}
