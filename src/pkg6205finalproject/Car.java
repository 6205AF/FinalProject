
import java.awt.Color;
import java.awt.Graphics;


public class Car extends Vehicle{
	public double px,py;//current position
	public double v; //current velocity
	public double length;//Vehicle length
	public double width;//Vehicle width
	public double acceleration;//maximum acceleration per tick
	public double deceleration;//maximum deceleration per tick
	public String direction;
	public Driver driver;//driver
	public int SignalSwitchedDuration = 0;//Time(ticks) after switch on turn signal
	public int lane;//current (or former in move) lane
	
	public boolean hasMoved;//whether has moved in current tick
	
	public Car() {}

	// initialize
	public Car(double length, double width, double acceleration, double deceleration, int lane) {
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
        
        public int getX(){
            int px = (int)this.getPx();
            return px;
        }
        
        public int getY(){
            int py = (int)this.getPy();
            return py;
        }
        
        public void setX(int newx){
            this.setPx(newx+0.0);
        }
        
        public int getSpeed(){
            int v = (int)this.getV();
            return v;
        }
}
