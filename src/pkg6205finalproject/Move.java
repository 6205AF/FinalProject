import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Move {

	//move vehicles
	public void move(TrafficSimulation trafficSimulation) {
		setAllUnmoved(trafficSimulation);
		int[] n1 = {0,0}, n2 = {1,0}, n3 = {2,0}, n4 = {3,0}, n5 = {4,0};
		while (n1[1] < trafficSimulation.vehicles.get(0).size() ||
				n2[1] < trafficSimulation.vehicles.get(1).size() ||
				n3[1] < trafficSimulation.vehicles.get(2).size() ||
				n4[1] < trafficSimulation.vehicles.get(3).size() ||
				n4[1] < trafficSimulation.vehicles.get(4).size()) {
			moveHelper(findMostForward(n1,findMostForward(n2,findMostForward(n3,findMostForward(n4,n5, trafficSimulation.vehicles), trafficSimulation.vehicles), trafficSimulation.vehicles), trafficSimulation.vehicles), trafficSimulation.vehicles,trafficSimulation);
		}
		spawn(trafficSimulation);
	}

	public void setAllUnmoved(TrafficSimulation trafficSimulation) {
		for (int i=0; i<trafficSimulation.vehicles.size();i++) {
			for (int j=0; j<trafficSimulation.vehicles.get(i).size();j++) {
				trafficSimulation.vehicles.get(i).get(j).setHasMoved(false);
			}
		}
	}

	//helper function for move()
	public void moveHelper(int[] n,HashMap<Integer,ArrayList<Vehicle>> vehicles, TrafficSimulation trafficSimulation) {
		Vehicle a = vehicles.get(n[0]).get(n[1]);
		//if this vehicle reaches the end of the road
		System.out.println("[" + n[0]+ "," + n[1] + "]");
		a.print();
		System.out.println();
		if (a.getPx() >= TrafficSimulation.roadLength) {
			remove(n, vehicles);
			trafficSimulation.counter--;
		}
		//call helper function if the vehicle is in the most left lane
		else if (n[0] == 0) {
			LeftSideHelper(n, vehicles, trafficSimulation);
		}
		//call helper function if the vehicle is in the most right lane
		else if (n[0] == TrafficSimulation.lanes-1) {
			RightSideHelper(n, vehicles, trafficSimulation);
		}
		else {
			Vehicle leftFront = null;
			Vehicle rightFront = null;
			if (vehicles.get(n[0]-1).size()==0) {}
			else if (vehicles.get(n[0]-1).size()==1) {rightFront = vehicles.get(n[0]-1).get(0);}
			else {
				int i = 0;
				while (i<vehicles.get(n[0]-1).size()-1 && vehicles.get(n[0]-1).get(i+1).getPx() > a.getPx()) {i++;}
				if (vehicles.get(n[0]-1).get(i+1).getPx() > a.getPx()) {i++;}
				leftFront = vehicles.get(n[0]+1).get(i);
			}
			if (vehicles.get(n[0]+1).size()==0) {}
			else if (vehicles.get(n[0]+1).size()==1) {rightFront = vehicles.get(n[0]+1).get(0);}
			else {
				int i = 0;
				while (i<vehicles.get(n[0]+1).size()-1 && vehicles.get(n[0]+1).get(i+1).getPx() > a.getPx()) {i++;}
				if (vehicles.get(n[0]+1).get(i+1).getPx()> a.getPx()) {i++;}
				rightFront = vehicles.get(n[0]+1).get(i);
			}
			//if this vehicle is the most forward vehicle in current lane
			if(n[1]==0) {
				if (a.getDirection() == "forward") {
					speedUp(a);
					a.setPx(a.getPx()+a.getV());
				}
				else if(a.getDirection() == "left") {
					judgeForward(a,leftFront);
					a.setPx(a.getPx()+0.8*a.getV());
					a.setPy(a.getPy()-0.6*a.getV());
				}
				else {
					judgeForward(a,rightFront);
					a.setPx(a.getPx()+0.8*a.getV());
					a.setPy(a.getPy()+0.6*a.getV());
				}
			}
			//there is vehicle in front of a
			else {
				Vehicle front = vehicles.get(n[0]).get(n[1]-1);
				//if this vehicle is moving forward
				if (a.getDirection() == "forward") {
					//if the driver in vehicle a is an egoistic driver and the driver in front vehicle is not
					if (a.getDriver().getMaxSpeed() > front.getDriver().getMaxSpeed()) {
						if (leftFront == null || leftFront.getPx() >= rightFront.getPx()) {
							judge(a, front, leftFront, n, vehicles);
						}
						else {
							judge(a, front, rightFront, n, vehicles);
						}
					}
					else {
						//judge the situation
						judgeForward(a, front);
					}
				}
				//a is cutting to left
				else if (a.getDirection() == "left"){
					judgeCuttingIn(a,front,leftFront);
					a.setPx(a.getPx()+0.8*a.getV());
					a.setPy(a.getPy()-0.6*a.getV());
					//if a has completed cutting in
					if (a.getPy() <= (0.1+n[0]-1)*Road.WIDTH) {
						a.setPy((0.1+n[0]-1)*Road.WIDTH);
						a.setDirection("forward");
						remove(n,vehicles);
						n[1]--;
					}
				}
				//a is cutting to right
				else {
					judgeCuttingIn(a,front,rightFront);
					a.setPx(a.getPx()+0.8*a.getV());
					a.setPy(a.getPy()+0.6*a.getV());
					//if a has completed cutting in
					if (a.getPy() >= (0.1+n[0]+1)*Road.WIDTH) {
						a.setPy((0.1+n[0]-1)*Road.WIDTH);
						a.setDirection("forward");
						remove(n,vehicles);
						n[1]--;
					}
				}
			}
			a.setHasMoved(true);
			n[1]++;
		}
	}

	public void LeftSideHelper(int[] n,HashMap<Integer,ArrayList<Vehicle>> vehicles, TrafficSimulation trafficSimulation) {
		Vehicle a = vehicles.get(n[0]).get(n[1]);
		//get the information of the other lane
		Vehicle rightFront = null;
		if (vehicles.get(n[0]+1).size()==0) {}
		else if (vehicles.get(n[0]+1).size()==1) {rightFront = vehicles.get(n[0]+1).get(0);}
		else {
			//problem here: find right first vehicle from left lane
                        int i = 0;
			while (i<vehicles.get(n[0]+1).size()-1 && vehicles.get(n[0]+1).get(i+1).getPx() > a.getPx()) {i++;}
			//if (vehicles.get(n[0]+1).get(i+1).getPx() > a.getPx()) {i++;}
			rightFront = vehicles.get(n[0]+1).get(i);
		}
		//if this vehicle is the most forward vehicle in the road
		if(n[1]==0) {
			if (a.getDirection() == "foward") {
				speedUp(a);
				a.setPx(a.getPx()+a.getV());
			}
			else {
				judgeForward(a,rightFront);
				a.setPx(a.getPx()+0.8*a.getV());
				a.setPy(a.getPy()+0.6*a.getV());
			}
		}
		//there is vehicle in front of a
		else {
			Vehicle front = vehicles.get(n[0]).get(n[1]-1);
			//if this vehicle is moving forward
			if (a.getDirection() == "foward") {
				//if the driver in vehicle a is an egoistic driver and the driver in front vehicle is not
				if (a.getDriver().getMaxSpeed() > front.getDriver().getMaxSpeed()) {
					judge(a, front, rightFront, n, vehicles);
				}
				else {
					//judge the situation
					judgeForward(a, front);
				}
			}
			//a is cutting to right
			else {
				judgeCuttingIn(a,front,rightFront);
				a.setPx(a.getPx()+0.8*a.getV());
				a.setPy(a.getPy()+0.6*a.getV());
				//if a has completed cutting in
				if (a.getPy() >= (0.1+n[0])*Road.WIDTH) {
					a.setPy((0.1+n[0])*Road.WIDTH);
					a.setDirection("forward");
					remove(n, vehicles);
				}
			}
		}
		a.setHasMoved(true);
		n[1]++;
	}


	public void RightSideHelper(int[] n,HashMap<Integer,ArrayList<Vehicle>> vehicles, TrafficSimulation trafficSimulation) {
		Vehicle a = vehicles.get(n[0]).get(n[1]);
		int i = 0;
		while (i<vehicles.get(n[0]-1).size() && vehicles.get(n[0]-1).get(i+1).getPx() > a.getPx()) {i++;}
		Vehicle leftFront = vehicles.get(n[0]-1).get(i);
		//if this vehicle is the most forward vehicle in the road
		if(n[1]==0) {
			if (a.getDirection() == "forward") {
				speedUp(a);
				a.setPx(a.getPx()+a.getV());
			}
			else {
				judgeForward(a,leftFront);
				a.setPx(a.getPx()+0.8*a.getV());
				a.setPy(a.getPy()-0.6*a.getV());
			}
		}
		//there is vehicle in front of a
		else {
			Vehicle front = vehicles.get(n[0]).get(n[1]-1);
			//if this vehicle is moving forward
			if (a.getDirection() == "forward") {
				//if the driver in vehicle a is an egoistic driver and the driver in front vehicle is not
				if (a.getDriver().getMaxSpeed() > front.getDriver().getMaxSpeed()) {
					judge(a, front, leftFront, n, vehicles);
				}
				else {
					//judge the situation
					judgeForward(a, front);
				}
			}
			//a is cutting to left
			else {
				judgeCuttingIn(a,front,leftFront);
				a.setPx(a.getPx()+0.8*a.getV());
				a.setPy(a.getPy()-0.6*a.getV());
				//if a has completed cutting in
				if (a.getPy() <= (0.1+n[0]-1)*Road.WIDTH) {
					a.setPy((0.1+n[0]-1)*Road.WIDTH);
					a.setDirection("forward");
					remove(n, vehicles);
					n[1]--;
				}
			}
		}
		a.setHasMoved(true);
		n[1]++;
	}

	//judge situation when is driving forward
	public void judgeForward(Vehicle a, Vehicle b) {
		//if there is no vehicle in front of a or a is in front of b
		if (b == null || a.getPx() >= b.getPx()) {
			speedUp(a);
		}
		//if a is too close to b
		else if (a.getPx() + 1.5*a.getLength() + a.getV() - a.getDeceleration() >= b.getPx()) {
			brake(a);
		}
		//when a need to catch up front car
		else {
			catchUp(a,b);
		}
	}

	//judge situation when is cutting in
	public void judgeCuttingIn(Vehicle a, Vehicle front, Vehicle side) {
		//if there is no vehicle in the side
		if (side == null) {
			judgeForward(a,front);
		}
		//if there is no vehicle in front of a
		else if (front == null) {
			judgeForward(a,side);
		}
		//both lanes have vehicle in front of a
		else {
			//the maximum acceptable speed for a to cutting in
			double maxSpeed = (Math.min(front.getPx(), side.getPx()) - a.getPx() - a.getLength());
			if(a.getV()>=maxSpeed) {
				a.setV(Math.max(a.getV()-a.getDeceleration(), maxSpeed));
			}
			else {
				a.setV(Math.min(a.getV()+a.getAcceleration(), maxSpeed));
			}
		}
	}

	//judge situation for egoistic driver
	public void judge(Vehicle a, Vehicle front, Vehicle side, int[] n, HashMap<Integer,ArrayList<Vehicle>> vehicles) {
		//a is still far from front Vehicle
		if (a.getPx() + 1.5*a.getLength() < front.getPx()) {
			a.setDirection("front");
			catchUp(a, front);
			a.setPx(a.getPx()+a.getV());
		}
		//there is enough space between a and side vehicle
		else if (a.getV() + 1.5*a.getLength() < side.getPx()) {
			//determine the cut in direction and add a to that ArrayList<Vehicle>
			if (a.getPy() > side.getPy()) {
				judgeCuttingIn(a,front,side);
				a.setDirection("left");
				a.setHasMoved(true);
				int i=0;
				while (i<vehicles.get(n[0]-1).size() && a.getPx() < vehicles.get(n[0]-1).get(i).getPx()) {i++;}
				vehicles.get(n[0]-1).add(i,a);
			}
			else {
				a.setDirection("right");
				int i=0;
				while (i<vehicles.get(n[0]+1).size() && a.getPx() < vehicles.get(n[0]+1).get(i).getPx() ) {i++;}
				vehicles.get(n[0]+1).add(i,a);
			}
		}
		//no chance to cut in
		else {
			judgeForward(a,front);
			a.setPx(a.getPx()+a.getV());;
		}
	}

	//brake
	public void brake(Vehicle a) {
		//full brake
		a.setV(Math.max(a.getV() - a.getDeceleration(), 0));
	}

	//speed up
	public void speedUp(Vehicle a) {
		a.setV(Math.min(a.getV() + a.getAcceleration(), a.getDriver().getMaxSpeed()));
	}

	//catch up other car
	public void catchUp(Vehicle a, Vehicle b) {
		double dist = b.getPx() -1.5*a.getLength() - a.getPx();
		//a is able to keep distance with b
		if(a.getV() - a.getDeceleration() <= dist && a.getV() + a.getAcceleration() >= dist) {
			a.setV(dist);
		}
		//a is too far to b
		else {
			speedUp(a);
		}
	}

	//find the most forward vehicle
	public int[] findMostForward(int[] n1,int[] n2,HashMap<Integer,ArrayList<Vehicle>> vehicles) {
		//if either list reaches end
		if (vehicles.get(n1[0]).size()==n1[1]) {
			return n2;
		}
		else if (vehicles.get(n2[0]).size()==n2[1]) {
			return n1;
		}
		//if two vehicles are the same
		else if (vehicles.get(n1[0]).get(n1[1]).equals(vehicles.get(n2[0]).get(n2[1]))) {
			//if vehicle direction is left
			if (vehicles.get(n1[0]).get(n1[1]).getDirection() == "left") {
				n1[1]++;
				return n2;
			}
			else {
				n2[1]++;
				return n1;
			}
		}
		else if (vehicles.get(n1[0]).get(n1[1]).isHasMoved()) {
			return n2;
		}
		else if (vehicles.get(n1[0]).get(n1[1]).getPx()>=vehicles.get(n2[0]).get(n2[1]).getPx()) {
			return n1;
		}
		return n2;
	}

	////spawn new vehicle in a random possible lane
	public void spawn(TrafficSimulation trafficSimulation) {
		Random a = new Random();
		double vehicleType = a.nextDouble();
		double driverType = a.nextDouble();
		Driver d = null;
		Vehicle newVehicle = null;
		//decide which kind of vehicle should be spawned
		if (vehicleType < trafficSimulation.truckRatio) {
			newVehicle =  new Truck(trafficSimulation.truckLength, trafficSimulation.width, trafficSimulation.acceleration*trafficSimulation.truckSpeedRatio,trafficSimulation.deceleration*trafficSimulation.truckSpeedRatio, 0);
		}
		else {
			newVehicle = new Car(trafficSimulation.carLength, trafficSimulation.width, trafficSimulation.acceleration, trafficSimulation.deceleration, 0);
		}	
		//decide which kind of driver is in the vehicle	
		if (driverType <= trafficSimulation.altruisticDriverRatio) {
			d = new Driver(true, trafficSimulation.cutInWaitingTimeA, trafficSimulation.maxSpeedA);
		}
		else {
			d = new Driver(false, trafficSimulation.cutInWaitingTimeE, trafficSimulation.maxSpeedE);
		}
		newVehicle.setDriver(d);
		//spawn new vehicle in a random possible lane
		ArrayList<Integer> l = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4));
		while (l.size() != 0) {
			int i = a.nextInt(l.size());
			if (trafficSimulation.vehicles.get(l.get(i)).size()==0) {
				newVehicle.setPy((l.get(i)+0.1)*trafficSimulation.road.LANE_WIDTH);
				trafficSimulation.vehicles.get(l.get(i)).add(newVehicle);
				trafficSimulation.counter++;
				break;
			}
			Vehicle lastVehicle = trafficSimulation.vehicles.get(l.get(i)).get(trafficSimulation.vehicles.get(l.get(i)).size()-1);
			if (newVehicle.getPx() + newVehicle.getLength() < lastVehicle.getPx()) {
				newVehicle.setPy((l.get(i)+0.1)*trafficSimulation.road.LANE_WIDTH);
				trafficSimulation.vehicles.get(l.get(i)).add(newVehicle);
				trafficSimulation.counter++;
				break;
			}
			else {
				l.remove(i);
			}
		}
	}

	//remove a vehicle from the list of vehicles
	public void remove(int[] n, HashMap<Integer,ArrayList<Vehicle>> vehicles) {
		vehicles.get(n[0]).remove(n[1]);
	}

	//Whether there is special vehicle on road
	public boolean hasSpecialVehicle(Vehicle[] vehicles)  {
		for (int i = 0; i < vehicles.length; i++) {
			//----------------------
			//            if (vehicles[i].getClass() == this.specialVehicle.getClass()) {
			//                return true;
			//            }
		}
		return false;
	}
}
