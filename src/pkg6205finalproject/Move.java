import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/*
 * comment lines 281-300 to stop forcing vehicles merge to left, also need to modify Road.java
 * comment lines 546-574 to stop spawning special vehicles
 * comment lines 579-584 and uncomment line 592 to stop spawning turcks
 */

public class Move {

	//move vehicles
	public void move(TrafficSimulation trafficSimulation) {
		updateFlow(trafficSimulation);
		reorder(trafficSimulation);
		setAllUnmoved(trafficSimulation);
		int[] n1 = {0,0}, n2 = {1,0}, n3 = {2,0}, n4 = {3,0}, n5 = {4,0};
		while (n1[1] < trafficSimulation.vehicles.get(0).size() ||
				n2[1] < trafficSimulation.vehicles.get(1).size() ||
				n3[1] < trafficSimulation.vehicles.get(2).size() ||
				n4[1] < trafficSimulation.vehicles.get(3).size() ||
				n5[1] < trafficSimulation.vehicles.get(4).size()) {
			moveHelper(findMostForward(n1,findMostForward(n2,findMostForward(n3,findMostForward(n4,n5, trafficSimulation.vehicles), trafficSimulation.vehicles), trafficSimulation.vehicles), trafficSimulation.vehicles), trafficSimulation.vehicles,trafficSimulation);
		}
		spawn(trafficSimulation);
	}

	//helper function for move()
	public void moveHelper(int[] n,HashMap<Integer,ArrayList<Vehicle>> vehicles, TrafficSimulation trafficSimulation) {
		Vehicle a = vehicles.get(n[0]).get(n[1]);
		//if this vehicle reaches the end of the road
		System.out.println("[" + n[0]+ "," + n[1] + "]");
		a.print();
		System.out.println();
		if (a.getPx() >= TrafficSimulation.roadLength) {
			if (a.getDirection() =="left") {
				a.setDirection("forward");
				remove(n,vehicles);
			}
			else if (a.getDirection() == "right") {
				a.setDirection("forward");
				remove(n,vehicles);
			}
			else {
				remove(n, vehicles);
				trafficSimulation.counter--;
				for (int i=0;i<trafficSimulation.flow.size();i++) {
					trafficSimulation.flow.set(i, trafficSimulation.flow.get(i)+1);
				}
			}
			return;
		}
		else if (a.getClass().equals(new SpecialVehicle().getClass())) {
			if (n[1]==0) {
				judgeForward(a,null);
				a.setPx(a.getPx()+a.getV());
			}
			else {
				Vehicle front = vehicles.get(n[0]).get(n[1]-1);
				judgeForward(a,front);
				a.setPx(a.getPx()+a.getV());
			}
		}
		//call helper function if the vehicle is in the most left lane
		else if (n[0] == 0) {
			LeftSideHelper(n, vehicles, trafficSimulation);
			return;
		}
		//call helper function if the vehicle is in the most right lane
		else if (n[0] == TrafficSimulation.lanes-1) {
			RightSideHelper(n, vehicles, trafficSimulation);
			return;
		}
		//the vehicle is in middle lanes
		else {
			Vehicle leftFront = null;
			Vehicle leftBehind = null;
			int i = 0;
			if (vehicles.get(n[0]-1).size()==0) {}
			else {
				while (i<vehicles.get(n[0]-1).size()-1 && vehicles.get(n[0]-1).get(i+1).getPx() > a.getPx()) {i++;}
				if (vehicles.get(n[0]-1).get(i).equals(a) && i < vehicles.get(n[0]-1).size()-1) {
					leftBehind = vehicles.get(n[0]-1).get(i+1);
				}
				else if (vehicles.get(n[0]-1).get(i).getPx()<a.getPx()) {
					leftBehind = vehicles.get(n[0]-1).get(i);
				}
				else {
					leftFront = vehicles.get(n[0]-1).get(i);
					if (vehicles.get(n[0]-1).size()==0 || i==vehicles.get(n[0]-1).size()-1) {}
					else {
						leftBehind = vehicles.get(n[0]-1).get(i+1);
					} 
					if (leftBehind != null && leftBehind.equals(a) && i<vehicles.get(n[0]-1).size()-2) {
						leftBehind = vehicles.get(n[0]-1).get(i+2);
					}
				}
			}
			Vehicle rightFront = null;
			Vehicle rightBehind = null;
			int j = 0;
			if (vehicles.get(n[0]+1).size()==0) {}
			else {
				while (j<vehicles.get(n[0]+1).size()-1 && vehicles.get(n[0]+1).get(j+1).getPx() > a.getPx()) {j++;}
				rightFront = vehicles.get(n[0]+1).get(j);
			}
			if (vehicles.get(n[0]+1).get(j).getPx()<a.getPx()) {
				rightBehind = vehicles.get(n[0]+1).get(j);
			}
			else if (vehicles.get(n[0]+1).size()==0 || j==vehicles.get(n[0]+1).size()-1) {}
			else {
				rightBehind = vehicles.get(n[0]+1).get(j+1);
			} 
			if (rightBehind != null && rightBehind.equals(a) && j<vehicles.get(n[0]+1).size()-2) {
				rightBehind = vehicles.get(n[0]+1).get(j+2);
			}
			//if this vehicle is the most forward vehicle in current lane
			if(n[1]==0) {
				if (a.getDirection().equals("forward")) {
					speedUp(a);
					a.setPx(a.getPx()+a.getV());
				}
				else if (a.getDirection().equals("right")){
					judgeCuttingIn(a,null, rightFront, rightBehind);
					a.setPx(a.getPx()+0.8*a.getV());
					a.setPy(a.getPy()+0.6*a.getV());
					//if a has completed cutting in
					if (a.getPy() >= (0.1+n[0]+1)*Road.LANE_WIDTH) {
						a.setPy((0.1+n[0]+1)*Road.LANE_WIDTH);
						a.setDirection("forward");
						remove(n, vehicles);
						n[1]--;
					}
				}
				else {
					judgeCuttingIn(a,null, leftFront, leftBehind);
					a.setPx(a.getPx()+0.8*a.getV());
					a.setPy(a.getPy()-0.6*a.getV());
					//if a has completed cutting in
					if (a.getPy() <= (0.1+n[0]-1)*Road.LANE_WIDTH) {
						a.setPy((0.1+n[0]-1)*Road.LANE_WIDTH);
						a.setDirection("forward");
						remove(n, vehicles);
						n[1]--;
					}
				}
			}
			//there is vehicle in front of a
			else {
				Vehicle front = vehicles.get(n[0]).get(n[1]-1);
				//if this vehicle is moving forward
				if (a.getDirection().equals("forward")) {
					judgeForward(a,front);
					a.setPx(a.getPx()+a.getV());
				}
				else if (a.getDirection().equals("right")){
					judgeCuttingIn(a,front, rightFront, rightBehind);
					a.setPx(a.getPx()+0.8*a.getV());
					a.setPy(a.getPy()+0.6*a.getV());
					//if a has completed cutting in
					if (a.getPy() >= (0.1+n[0]+1)*Road.LANE_WIDTH) {
						a.setPy((0.1+n[0]+1)*Road.LANE_WIDTH);
						a.setDirection("forward");
						remove(n, vehicles);
						n[1]--;
					}
				}
				else {
					judgeCuttingIn(a,front, leftFront, leftBehind);
					a.setPx(a.getPx()+0.8*a.getV());
					a.setPy(a.getPy()-0.6*a.getV());
					//if a has completed cutting in
					if (a.getPy() <= (0.1+n[0]-1)*Road.LANE_WIDTH) {
						a.setPy((0.1+n[0]-1)*Road.LANE_WIDTH);
						a.setDirection("forward");
						remove(n, vehicles);
						n[1]--;
					}
				}
			}
		}
		a.setHasMoved(true);
		n[1]++;
	}

	public void LeftSideHelper(int[] n,HashMap<Integer,ArrayList<Vehicle>> vehicles, TrafficSimulation trafficSimulation) {
		//get the information of the other lane
		Vehicle a = vehicles.get(n[0]).get(n[1]);
		Vehicle rightFront = null;
		Vehicle rightBehind = null;
		int i = 0;
		if (vehicles.get(n[0]+1).size()==0) {}
		else {
			while (i<vehicles.get(n[0]+1).size()-1 && vehicles.get(n[0]+1).get(i+1).getPx() > a.getPx()) {i++;}
			rightFront = vehicles.get(n[0]+1).get(i);
		}
		if (vehicles.get(n[0]+1).get(i).getPx()<a.getPx()) {
			rightBehind = vehicles.get(n[0]+1).get(i);
		}
		else if (vehicles.get(n[0]+1).size()==0 || i==vehicles.get(n[0]+1).size()-1) {}
		else {
			rightBehind = vehicles.get(n[0]+1).get(i+1);
		} 
		if (rightBehind != null && rightBehind.equals(a) && i<vehicles.get(n[0]+1).size()-2) {
			rightBehind = vehicles.get(n[0]+1).get(i+2);
		}
		//there is no vehicle in front of a
		if(n[1]==0) {
			if (a.getDirection().equals("forward")) {
				speedUp(a);
				a.setPx(a.getPx()+a.getV());
			}
			else {
				judgeCuttingIn(a,null, rightFront, rightBehind);
				a.setPx(a.getPx()+0.8*a.getV());
				a.setPy(a.getPy()+0.6*a.getV());
				//if a has completed cutting in
				if (a.getPy() >= (0.1+n[0]+1)*Road.LANE_WIDTH) {
					a.setPy((0.1+n[0]+1)*Road.LANE_WIDTH);
					a.setDirection("forward");
					remove(n, vehicles);
					n[1]--;
				}
			}
		}
		//there is vehicle in front of a
		else {
			Vehicle front = vehicles.get(n[0]).get(n[1]-1);
			//if this vehicle is moving forward
			if (a.getDirection().equals("forward")) {
				//judge the situation
				judgeForward(a, front);
				a.setPx(a.getPx()+a.getV());
			}
			//a is cutting to left
			else {
				judgeCuttingIn(a,front,rightFront,rightBehind);
				a.setPx(a.getPx()+0.8*a.getV());
				a.setPy(a.getPy()-0.6*a.getV());
				//if a has completed cutting in
				if (a.getPy() >= (0.1+n[0]+1)*Road.LANE_WIDTH) {
					a.setPy((0.1+n[0]+1)*Road.LANE_WIDTH);
					a.setDirection("forward");
					remove(n, vehicles);
					n[1]--;
				}
			}
		}
		a.setHasMoved(true);
		n[1]++;
	}

	public void RightSideHelper(int[] n,HashMap<Integer,ArrayList<Vehicle>> vehicles, TrafficSimulation trafficSimulation) {
		Vehicle a = vehicles.get(n[0]).get(n[1]);
		Vehicle leftFront = null;
		Vehicle leftBehind = null;
		int i = 0;
		if (vehicles.get(n[0]-1).size()==0) {}
		else {
			while (i<vehicles.get(n[0]-1).size()-1 && vehicles.get(n[0]-1).get(i+1).getPx() > a.getPx()) {i++;}
			if (vehicles.get(n[0]-1).get(i).equals(a) && i < vehicles.get(n[0]-1).size()-1) {
				leftBehind = vehicles.get(n[0]-1).get(i+1);
			}
			else if (vehicles.get(n[0]-1).get(i).getPx()<a.getPx()) {
				leftBehind = vehicles.get(n[0]-1).get(i);
			}
			else {
				leftFront = vehicles.get(n[0]-1).get(i);
				if (vehicles.get(n[0]-1).size()==0 || i==vehicles.get(n[0]-1).size()-1) {}
				else {
					leftBehind = vehicles.get(n[0]-1).get(i+1);
				} 
				if (leftBehind != null && leftBehind.equals(a) && i<vehicles.get(n[0]-1).size()-2) {
					leftBehind = vehicles.get(n[0]-1).get(i+2);
				}
			}
		}
		if (trafficSimulation.situationNumber == 1 || trafficSimulation.situationNumber == 3){
			//if this vehicle has to merge left
			if (a.getPx()>=400 && a.getDirection().equals("forward")) {
				if(leftFront==null) {
					vehicles.get(n[0]-1).add(0,a);
				}
				else {
					vehicles.get(n[0]-1).add(i+1,a);
				}
				a.setDirection("left");
				a.setV(trafficSimulation.deceleration);
				judgeCuttingIn(a,null,leftFront,leftBehind);
				a.setPx(a.getPx()+0.8*a.getV());
				a.setPy(a.getPy()-0.6*a.getV());
				//if a has completed cutting in
				if (a.getPy() <= (0.1+n[0]-1)*Road.LANE_WIDTH) {
					a.setPy((0.1+n[0]-1)*Road.LANE_WIDTH);
					a.setDirection("forward");
					remove(n, vehicles);
					n[1]--;
				}
			}
		}

		//if this vehicle is the most forward vehicle in the road
		if(n[1]==0) {
			if (a.getDirection().equals("forward")) {
				speedUp(a);
				a.setPx(a.getPx()+a.getV());
			}
			else {
				judgeCuttingIn(a,null, leftFront, leftBehind);
				a.setPx(a.getPx()+0.8*a.getV());
				a.setPy(a.getPy()-0.6*a.getV());
				//if a has completed cutting in
				if (a.getPy() <= (0.1+n[0]-1)*Road.LANE_WIDTH) {
					a.setPy((0.1+n[0]-1)*Road.LANE_WIDTH);
					a.setDirection("forward");
					remove(n, vehicles);
					n[1]--;
				}
			}
		}
		//there is vehicle in front of a
		else {
			Vehicle front = vehicles.get(n[0]).get(n[1]-1);
			//if this vehicle is moving forward
			if (a.getDirection().equals("forward")) {
				//if the driver in vehicle a is an egoistic driver and the driver in front vehicle is not
				if (a.getDriver().getMaxSpeed() > front.getDriver().getMaxSpeed()) {
					judge(a, front, leftFront, leftBehind, n, vehicles);
				}
				else {
					//judge the situation
					judgeForward(a, front);
					a.setPx(a.getPx()+a.getV());
				}
			}
			//a is cutting to left
			else {
				judgeCuttingIn(a,front,leftFront,leftBehind);
				a.setPx(a.getPx()+0.8*a.getV());
				a.setPy(a.getPy()-0.6*a.getV());
				//if a has completed cutting in
				if (a.getPy() <= (0.1+n[0]-1)*Road.LANE_WIDTH) {
					a.setPy((0.1+n[0]-1)*Road.LANE_WIDTH);
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
		//if there is no vehicle in front of a or front vehicle is cutting in but there is no enough space
		if (b == null || (b.getDirection()!="forward" && a.getPx()+a.getLength()>b.getPx())) {
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
	public void judgeCuttingIn(Vehicle a, Vehicle front, Vehicle sideFront, Vehicle sideBehind) {
		//if there is no vehicles behind in side lane
		if (sideBehind == null) {
			judgeForward(a,sideFront);
		}
		else if (front == null && sideFront == null || a.getPx()>=sideFront.getPx()) {
			speedUp(a);
		}
		else if (a.equals(sideFront)) {
			speedUp(a);
		}
		else if (a.getDirection()=="left" && sideBehind.getDirection()=="right" ||
				a.getDirection()=="right"&& sideBehind.getDirection()=="left") {
			judgeForward(a,sideFront);
		}
		//if there is no space for a to cut in
		else if(sideBehind.getPx()+sideBehind.getLength()>a.getPx()) {
			brake(a);
		}
		else {
			double frontPx;
			double sideFrontPx;
			//if there is no vehicle in front of a in side lane
			if (sideFront == null) {
				sideFrontPx = 3000;
			}
			else {sideFrontPx = sideFront.getPx();}
			//if there is no vehicle in front of a
			if (front == null) {
				frontPx = 3000;
			}
			else {frontPx = front.getPx();}
			//the maximum acceptable speed for a to cutting in	
			double maxSpeed = (Math.min(frontPx, sideFrontPx) - a.getPx() - a.getLength());
			if (maxSpeed <= 0) {
				a.setV(0);
			}
			else if(a.getV()>=maxSpeed) {
				a.setV(Math.min(a.getDriver().getMaxSpeed(), maxSpeed));
			}
			else {
				a.setV(Math.min(Math.min(a.getV()+a.getAcceleration(), maxSpeed),a.getDriver().getMaxSpeed()));
			}
		}
	}

	//judge situation for egoistic driver
	public void judge(Vehicle a, Vehicle front, Vehicle sideFront, Vehicle sideBehind, int[] n, HashMap<Integer,ArrayList<Vehicle>> vehicles) {
		//a is still far from front Vehicle
		if (a.getPx() + 1.5*a.getLength() < front.getPx()) {
			a.setDirection("front");
			catchUp(a, front);
			a.setPx(a.getPx()+a.getV());
		}
		//there is enough space between a and side vehicle
		else if (a.getV() + 1.5*a.getLength() < sideFront.getPx()) {
			//determine the cut in direction and add a to that ArrayList<Vehicle>
			if (a.getPx() > sideFront.getPx()) {
				judgeCuttingIn(a,front,sideFront,sideBehind);
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
		//a.setV(Math.max(a.getV() - a.getDeceleration(), 0));
		a.setV(0);
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
			a.setV(Math.max(dist, 0));
		}
		//a is too far to b
		else {
			speedUp(a);
		}
	}

	//force all vehicles in this lane to switch lane
	public void forceSwitch(int k,TrafficSimulation ts) {
		int j=k-1,j1=0;
		int l=k+1,l1=0;
		int k1=0;
		while (k1 < ts.vehicles.get(k).size()) {
			Vehicle a = ts.vehicles.get(k).get(k1);
			Vehicle leftFront = null;
			Vehicle rightFront = null;
			while (j1<ts.vehicles.get(j).size()-1 && ts.vehicles.get(j).get(j1+1).getPx() > a.getPx()) {
				j1++;
			}
			leftFront = ts.vehicles.get(j).get(j1);
			while (l1<ts.vehicles.get(l).size()-1 && ts.vehicles.get(l).get(l1+1).getPx() > a.getPx()) {
				l1++;
			}
			rightFront = ts.vehicles.get(l).get(l1);
			if (leftFront.getPx() >= rightFront.getPx()) {
				a.setDirection("left");
				ts.vehicles.get(j).add(++j1, a);
			}
			else {
				a.setDirection("right");
				ts.vehicles.get(l).add(++l1, a);
			}
			k1++;
		}
	}
	//find the most forward vehicle
	public int[] findMostForward(int[] n1,int[] n2,HashMap<Integer,ArrayList<Vehicle>> vehicles) {
		//if all vehicles in n1 has moved or n1 has moved
		if (vehicles.get(n1[0]).size()<=n1[1]) {
			return n2;
		}
		//if all vehicles in n2 has moved
		else if (vehicles.get(n2[0]).size()<=n2[1]) {
			return n1;
		}
		//if two vehicles are the same(this vehicle is cutting to left or right)
		else if (vehicles.get(n1[0]).get(n1[1]).equals(vehicles.get(n2[0]).get(n2[1]))) {
			//if vehicle is cutting left
			if (vehicles.get(n1[0]).get(n1[1]).getDirection() == "left") {
				n1[1]++;
				return n2;
			}
			//vehicle is cutting right
			else {
				n2[1]++;
				return n1;
			}
		}
		else if (vehicles.get(n1[0]).get(n1[1]).isHasMoved()) {
			n1[1]++;
			return findMostForward(n1,n2,vehicles);
		}
		else if (vehicles.get(n2[0]).get(n2[1]).isHasMoved()) {
			n2[1]++;
			return findMostForward(n1,n2,vehicles);
		}
		else if (vehicles.get(n1[0]).get(n1[1]).getPx()>=vehicles.get(n2[0]).get(n2[1]).getPx()) {
			return n1;
		}
		return n2;
	}

	////spawn new vehicle in a random possible lane
	public void spawn(TrafficSimulation trafficSimulation) {
		trafficSimulation.timeToSpawnSpecialVehicle++;
		Random a = new Random();
		ArrayList<Integer> l = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4));
		Driver d = null;
		Vehicle newVehicle = null;
		if (trafficSimulation.situationNumber == 3 || trafficSimulation.situationNumber ==4){
			if (trafficSimulation.timeToSpawnSpecialVehicle >= TrafficSimulation.specialVehicleFrequency) {
				newVehicle = new SpecialVehicle(trafficSimulation.carLength, trafficSimulation.width, trafficSimulation.acceleration, trafficSimulation.deceleration, 0);
				d = new Driver(false, trafficSimulation.cutInWaitingTimeE, trafficSimulation.maxSpeedE);
				newVehicle.setDriver(d);
				l = new ArrayList<Integer>(Arrays.asList(1,2));
				while (l.size() != 0) {
					int i = a.nextInt(l.size());
					if (trafficSimulation.vehicles.get(l.get(i)).size()==0) {
						newVehicle.setPy((l.get(i)+0.1)*trafficSimulation.road.LANE_WIDTH);
						trafficSimulation.vehicles.get(l.get(i)).add(newVehicle);
						trafficSimulation.counter++;
						trafficSimulation.timeToSpawnSpecialVehicle = 0;
						return;
					}
					else {
						Vehicle lastVehicle = trafficSimulation.vehicles.get(l.get(i)).get(trafficSimulation.vehicles.get(l.get(i)).size()-1);
						if (newVehicle.getPx() + newVehicle.getLength() < lastVehicle.getPx()) {
							newVehicle.setPy((l.get(i)+0.1)*trafficSimulation.road.LANE_WIDTH);
							forceSwitch(l.get(i),trafficSimulation);
							trafficSimulation.vehicles.get(l.get(i)).add(newVehicle);
							trafficSimulation.counter++;
							trafficSimulation.timeToSpawnSpecialVehicle = 0;
							return;
						}
						else l.remove(i);
					}
				}
				return;
			}
		}


		while (l.size() != 0) {
			double vehicleType = a.nextDouble();
			double driverType = a.nextDouble();
			//decide which kind of vehicle should be spawned
			if (trafficSimulation.situationNumber == 3){
				if (trafficSimulation.nextTruck) {
					newVehicle =  new Truck(trafficSimulation.truckLength, trafficSimulation.width, trafficSimulation.acceleration*trafficSimulation.truckSpeedRatio,trafficSimulation.deceleration*trafficSimulation.truckSpeedRatio, 0);
				}
				else {
					newVehicle = new Car(trafficSimulation.carLength, trafficSimulation.width, trafficSimulation.acceleration, trafficSimulation.deceleration, 0);
				}
			}

			//decide which kind of driver is in the vehicle	
			if (driverType <= trafficSimulation.altruisticDriverRatio) {
				d = new Driver(true, trafficSimulation.cutInWaitingTimeA, trafficSimulation.maxSpeedA);
			}
			else {
				d = new Driver(false, trafficSimulation.cutInWaitingTimeE, trafficSimulation.maxSpeedE);
			}
			if (trafficSimulation.situationNumber != 3){
				newVehicle = new Car(trafficSimulation.carLength, trafficSimulation.width, trafficSimulation.acceleration, trafficSimulation.deceleration, 0);
			}

			newVehicle.setDriver(d);
			//spawn new vehicle in a random possible lane
			while (l.size() != 0) {
				int i = a.nextInt(l.size());
				if (trafficSimulation.vehicles.get(l.get(i)).size()==0) {
					newVehicle.setPy((l.get(i)+0.1)*trafficSimulation.road.LANE_WIDTH);
					trafficSimulation.vehicles.get(l.get(i)).add(newVehicle);
					trafficSimulation.counter++;
					if(vehicleType < trafficSimulation.truckRatio) {
						trafficSimulation.nextTruck = true;
					}
					else {
						trafficSimulation.nextTruck = false;
					}
					break;
				}
				Vehicle lastVehicle = trafficSimulation.vehicles.get(l.get(i)).get(trafficSimulation.vehicles.get(l.get(i)).size()-1);
				if (newVehicle.getPx() + newVehicle.getLength() < lastVehicle.getPx()) {
					newVehicle.setPy((l.get(i)+0.1)*trafficSimulation.road.LANE_WIDTH);
					trafficSimulation.vehicles.get(l.get(i)).add(newVehicle);
					trafficSimulation.counter++;
					if(vehicleType < trafficSimulation.truckRatio) {
						trafficSimulation.nextTruck = true;
					}
					else {
						trafficSimulation.nextTruck = false;
					}
					break;
				}
				else {
					l.remove(i);
				}
			}
		}
	}


	//sort vehicles in ArrayList<Vehicle> by decending px(bubble sort)
	public void reorder(TrafficSimulation ts) {
		for(int i=0;i<ts.vehicles.size();i++) {
			for(int j=0;j<ts.vehicles.get(i).size()-1;j++) {
				if (ts.vehicles.get(i).get(j).getPx() < ts.vehicles.get(i).get(j+1).getPx()) {
					Vehicle temp = ts.vehicles.get(i).get(j);
					ts.vehicles.get(i).set(j, ts.vehicles.get(i).get(j+1));
					ts.vehicles.get(i).set(j+1, temp);
				}
			}
		}
	}

	//remove a vehicle from hashMap<Integer,ArrayList<Vehicle> by giving a int[2]
	public void remove(int[] n, HashMap<Integer,ArrayList<Vehicle>> vehicles) {
		vehicles.get(n[0]).remove(n[1]);
	}

	//update the flow
	public void updateFlow(TrafficSimulation ts) {
		if (ts.flow.size() == ts.specialVehicleFrequency) {
			ts.flow.remove(0);
		}
		ts.flow.add(0);
	}

	//set all vehicles unmoved
	public void setAllUnmoved(TrafficSimulation trafficSimulation) {
		for (int i=0; i<trafficSimulation.vehicles.size();i++) {
			for (int j=0; j<trafficSimulation.vehicles.get(i).size();j++) {
				trafficSimulation.vehicles.get(i).get(j).setHasMoved(false);
			}
		}
	}
}
