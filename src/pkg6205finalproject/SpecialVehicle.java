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
}
