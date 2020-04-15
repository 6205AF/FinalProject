import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Move {

    //move vehicles
    public void move(TrafficSimulation trafficSimulation) {
    	setAllUnmoved(trafficSimulation);
        int[] n1 = {0,0}, n2 = {1,0}, n3 = {2,0}, n4 = {3,0}, n5 = {4,0};
        while (n1[1] <= trafficSimulation.vehicles.get(0).size() &&
                n2[1] <= trafficSimulation.vehicles.get(1).size() &&
                n3[1] <= trafficSimulation.vehicles.get(2).size() &&
                n4[1] <= trafficSimulation.vehicles.get(3).size() &&
                n4[1] <= trafficSimulation.vehicles.get(4).size()) {
            moveHelper(findMostForward(n1,findMostForward(n2,findMostForward(n3,findMostForward(n4,n5, trafficSimulation.vehicles), trafficSimulation.vehicles), trafficSimulation.vehicles), trafficSimulation.vehicles), trafficSimulation.vehicles,trafficSimulation);
            spawn(trafficSimulation);
        }
    }
    
	public void setAllUnmoved(TrafficSimulation trafficSimulation) {
		for (int i=0; i<trafficSimulation.vehicles.size();i++) {
			for (int j=0; j<trafficSimulation.vehicles.get(i).size();j++) {
				trafficSimulation.vehicles.get(i).get(j).hasMoved = false;
			}
		}
	}

    //helper function for move()
    public void moveHelper(int[] n,HashMap<Integer,ArrayList<Vehicle>> vehicles, TrafficSimulation trafficSimulation) {
        Vehicle a = vehicles.get(n[0]).get(n[1]);
        //call helper function if the vehicle is in the most left lane
        if (n[0] == 0) {
            LeftSideHelper(n, vehicles, trafficSimulation);
        }
        //call helper function if the vehicle is in the most right lane
        else if (n[0] == TrafficSimulation.lanes) {
            RightSideHelper(n, vehicles, trafficSimulation);
        }

        //if this vehicle reaches the end of the road
        else if (a.px >= TrafficSimulation.roadLength) {
            remove(n, vehicles);
            trafficSimulation.counter--;
        }
        else {
            int i = 0;
            while (i<vehicles.get(n[0]-1).size() && vehicles.get(n[0]-1).get(i+1).px > a.px) {i++;}
            Vehicle leftFront = vehicles.get(n[0]-1).get(i);
            i = 0;
            while (i<vehicles.get(n[0]+1).size() && vehicles.get(n[0]+1).get(i+1).px > a.px) {i++;}
            Vehicle rightFront = vehicles.get(n[0]+1).get(i);
            //if this vehicle is the most forward vehicle in the road
            if(n[1]==0) {
                if (a.direction.equals(a.direction.forward)) {
                    speedUp(a);
                    a.px += a.v;
                }
                else if(a.direction.equals(a.direction.left)) {
                    judgeForward(a,leftFront);
                    a.px += 0.8*a.v;
                    a.py += 0.6*a.v;
                }
                else {
                    judgeForward(a,rightFront);
                    a.px += 0.8*a.v;
                    a.py -= 0.6*a.v;
                }
            }
            //there is vehicle in front of a
            else {
                Vehicle front = vehicles.get(n[0]).get(n[1]-1);
                //if this vehicle is moving forward
                if (a.direction.equals(a.direction.forward)) {
                    //if the driver in vehicle a is an egoistic driver and the driver in front vehicle is not
                    if (a.driver.maxSpeed > front.driver.maxSpeed) {
                        if (leftFront.px >= rightFront.px) {
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
                else if (a.direction.equals(a.direction.left)){
                    judgeCuttingIn(a,front,leftFront);
                    a.px += a.v*0.8;
                    a.py += a.v*0.6;
                    //if a has completed cutting in
                    if (a.py >= leftFront.py) {
                        a.py = leftFront.py;
                        a.direction = a.direction.forward;
                        remove(n,vehicles);
                    }
                }
                //a is cutting to right
                else {
                    judgeCuttingIn(a,front,rightFront);
                    a.px += a.v*0.8;
                    a.py -= a.v*0.6;
                    //if a has completed cutting in
                    if (a.py <= rightFront.py) {
                        a.py = rightFront.py;
                        a.direction = a.direction.forward;
                        remove(n,vehicles);
                    }
                }
            }
            a.hasMoved = true;
            n[1]++;
        }
    }

    public void LeftSideHelper(int[] n,HashMap<Integer,ArrayList<Vehicle>> vehicles, TrafficSimulation trafficSimulation) {
        Vehicle a = vehicles.get(n[0]).get(n[1]);
        //if this vehicle reaches the end of the road
        if (a.px >= TrafficSimulation.roadLength) {
            remove(n,vehicles);
            trafficSimulation.counter--;
        }
        else {
            int i = 0;
            while (i<vehicles.get(n[0]+1).size() && vehicles.get(n[0]+1).get(i+1).px > a.px) {i++;}
            Vehicle rightFront = vehicles.get(n[0]+1).get(i);
            //if this vehicle is the most forward vehicle in the road
            if(n[1]==0) {
                if (a.direction.equals(a.direction.forward)) {
                    speedUp(a);
                    a.px += a.v;
                }
                else {
                    judgeForward(a,rightFront);
                    a.px += 0.8*a.v;
                    a.py -= 0.6*a.v;
                }
            }
            //there is vehicle in front of a
            else {
                Vehicle front = vehicles.get(n[0]).get(n[1]-1);
                //if this vehicle is moving forward
                if (a.direction.equals(a.direction.forward)) {
                    //if the driver in vehicle a is an egoistic driver and the driver in front vehicle is not
                    if (a.driver.maxSpeed > front.driver.maxSpeed) {
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
                    a.px += a.v*0.8;
                    a.py -= a.v*0.6;
                    //if a has completed cutting in
                    if (a.py <= rightFront.py) {
                        a.py = rightFront.py;
                        a.direction = a.direction.forward;
                        remove(n, vehicles);
                    }
                }
            }
            a.hasMoved = true;
            n[1]++;
        }
    }

    public void RightSideHelper(int[] n,HashMap<Integer,ArrayList<Vehicle>> vehicles, TrafficSimulation trafficSimulation) {
        Vehicle a = vehicles.get(n[0]).get(n[1]);
        //if this vehicle reaches the end of the road
        if (a.px >= TrafficSimulation.roadLength) {
            remove(n, vehicles);
            trafficSimulation.counter--;
        }
        else {
            int i = 0;
            while (i<vehicles.get(n[0]-1).size() && vehicles.get(n[0]-1).get(i+1).px > a.px) {i++;}
            Vehicle leftFront = vehicles.get(n[0]-1).get(i);
            //if this vehicle is the most forward vehicle in the road
            if(n[1]==0) {
                if (a.direction.equals(a.direction.forward)) {
                    speedUp(a);
                    a.px += a.v;
                }
                else {
                    judgeForward(a,leftFront);
                    a.px += 0.8*a.v;
                    a.py += 0.6*a.v;
                }
            }
            //there is vehicle in front of a
            else {
                Vehicle front = vehicles.get(n[0]).get(n[1]-1);
                //if this vehicle is moving forward
                if (a.direction.equals(a.direction.forward)) {
                    //if the driver in vehicle a is an egoistic driver and the driver in front vehicle is not
                    if (a.driver.maxSpeed > front.driver.maxSpeed) {
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
                    a.px += a.v*0.8;
                    a.py += a.v*0.6;
                    //if a has completed cutting in
                    if (a.py >= leftFront.py) {
                        a.py = leftFront.py;
                        a.direction = a.direction.forward;
                        remove(n, vehicles);
                    }
                }
            }
            a.hasMoved = true;
            n[1]++;
        }
    }

    //judge situation when is driving forward
    public void judgeForward(Vehicle a, Vehicle b) {
        //when b is not in front of a (there is no vehicle in front of a)
        if(a.px >= b.px) {
            speedUp(a);
        }
        //if a is too close to b
        else if (a.px + 0.5*a.length <= b.px - b.length) {
            brake(a);
        }
        //when a need to catch up front car
        else {
            catchUp(a,b);
        }
    }

    //judge situation when is cutting in
    public void judgeCuttingIn(Vehicle a, Vehicle front, Vehicle side) {
        double maxSpeed = front.px - front.length*0.5 - a.px - a.length*0.5;
        if(a.v - a.deceleration>=maxSpeed) {
            brake(a);
        }
        else {
            judgeForward(a, side);
            a.v = Math.min(a.v, maxSpeed/0.8);
        }
    }

    //judge situation for egoistic driver
    public void judge(Vehicle a, Vehicle front, Vehicle side, int[] n, HashMap<Integer,ArrayList<Vehicle>> vehicles) {
        //a is still far from front Vehicle
        if (a.px + 0.5*a.length < front.px - front.length) {
            catchUp(a, front);
            a.px += a.v;
        }
        //there is enough space between a and side vehicle
        else if (a.px + 0.5*a.length < side.px - side.length) {
            //determine the cut in direction and add a to that Vehicle[]
            if (a.py > side.py) {
                judgeCuttingIn(a,front,side);
                a.direction = a.direction.left;
                a.hasMoved = true;
                int i=0;
                while (i<vehicles.get(n[0]-1).size() && a.px < vehicles.get(n[0]-1).get(i).px) {i++;}
                vehicles.get(n[0]-1).add(i,a);
            }
            else {
                a.direction = a.direction.right;
                int i=0;
                while (i<vehicles.get(n[0]+1).size() && a.px < vehicles.get(n[0]+1).get(i).px ) {i++;}
                vehicles.get(n[0]+1).add(i,a);
            }
        }
        //no chance to cut in
        else {
            judgeForward(a,front);
            a.px += a.v;
        }
    }

    //brake
    public void brake(Vehicle a) {
        //full brake
        a.v = Math.max(a.v - a.deceleration, 0);
    }

    //speed up
    public void speedUp(Vehicle a) {
        a.v = Math.min(a.v + a.acceleration, a.driver.maxSpeed);
    }

    //catch up other car
    public void catchUp(Vehicle a, Vehicle b) {
        double dist = b.px -b.length - a.px;
        //a is able to keep distance with b
        if(a.v - a.deceleration <= dist && a.v + a.acceleration >= dist) {
            a.v = dist;
        }
        //a is too far to b
        else {
            speedUp(a);
        }
    }

    //try to switch lane
    public void trySwitch(Vehicle a,Vehicle leftFront, Vehicle rightFront) {
        //if left side has more space than right side
        if(leftFront.px >= rightFront.px) {
            //there is enough space to cut in
            if(a.px + a.length <= leftFront.px) {
            	
            }
        }
    }

    //relative v between two vehicles
    public double relativeSpeed(Vehicle a, Vehicle b) {
        return a.v-b.v;
    }

    //find the most forward vehicle
    public int[] findMostForward(int[] n1,int[] n2,HashMap<Integer,ArrayList<Vehicle>> vehicles) {
        if (vehicles.get(n1[0]).size()==n1[1]) {
            return n2;
        }
        else if (vehicles.get(n2[0]).size()==n2[1]) {
            return n1;
        }
        //if two vehicles are the same
        if (vehicles.get(n1[0]).get(n1[1]).equals(vehicles.get(n2[0]).get(n2[1]))) {
            //if vehicle direction is left
            if (vehicles.get(n1[0]).get(n1[1]).direction == vehicles.get(n1[0]).get(n1[1]).direction.left) {
                return n2;
            }
            else return n1;
        }
        else if (vehicles.get(n1[0]).get(n1[1]).hasMoved) {
            return n2;
        }
        else if (vehicles.get(n1[0]).get(n1[1]).px>=vehicles.get(n2[0]).get(n2[1]).px) {
            return n1;
        }
        return n2;
    }

    ////spawn new vehicle in a random possible lane
	public void spawn(TrafficSimulation trafficSimulation) {
		Random a = new Random();
		double vehicleType = a.nextDouble();
		double driverType = a.nextDouble();
		Driver newDriver = null;
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
			newDriver = new Driver(true, trafficSimulation.cutInWaitingTimeA, trafficSimulation.maxSpeedA);
		}
		else {
			newDriver =  new Driver(false, trafficSimulation.cutInWaitingTimeE, trafficSimulation.maxSpeedE);
		}
		newVehicle.driver = newDriver;
		//spawn new vehicle in a random possible lane
		ArrayList<Integer> l = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4));
		while (l.size() != 0) {
			int i = a.nextInt(l.size());
			if (trafficSimulation.vehicles.get(l.get(i)).size()==0) {
				newVehicle.py = l.get(i)*trafficSimulation.roadWidth/trafficSimulation.lanes + 0.1*trafficSimulation.roadWidth;
				trafficSimulation.vehicles.get(l.get(i)).add(newVehicle);
				trafficSimulation.counter++;
				break;
			}
			Vehicle lastVehicle = trafficSimulation.vehicles.get(l.get(i)).get(trafficSimulation.vehicles.get(l.get(i)).size()-1);
			if (newVehicle.px + 0.5*newVehicle.length < lastVehicle.px - 0.5*lastVehicle.length) {
				newVehicle.py = l.get(i)*trafficSimulation.roadWidth/trafficSimulation.lanes + 0.1*trafficSimulation.roadWidth;
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
