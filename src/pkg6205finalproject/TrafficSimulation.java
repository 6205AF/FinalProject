
import java.awt.BorderLayout;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;

public class TrafficSimulation {
	//settings of vehicles
	public double carLength;//length of car and special vehicles(
	public double truckLength;//length of truck
	public double width;//vehicle width
	public double acceleration;//maximum Vehicle acceleration per tick
	public double deceleration;//maximum Vehicle deceleration per tick

	//settings for drivers
	public double maxSpeedA;//the maximum speed could reach for altruistic driver
	public int cutInWaitingTimeA;//start cutting in for x ticks after switch turn signal on for altruistic driver
	public double maxSpeedE;//the maximum speed could reach for egoistic driver
	public int cutInWaitingTimeE;//start cutting in for x ticks after switch turn signal on for egoistic driver
	public int specialVehicleFrequency;//special vehicles appear once every x ticks;

	//settings of road
	public int lanes = 5;//number of lanes of the road
	public int roadWidth;//the width in pixels for each road;
	public int roadLength;//the length in pixels for roads
	public double wetness;//a ratio influences the speed, acceleration and deceleration of vehicles
	public int[] laneInFixing;//the numbers of lane is in fixing
	public double speedLimit;//speed limit of the road

	//graphs
        JFrame frame = new JFrame("Traffic Simulation");
        Road road;

	//other settings
	public boolean hasSpeicalVehicle = false;//whether spawn special vehicle
	public boolean hasSpeicalTruck = false;//whether spawn special vehicle
	public boolean hasRoadInFixing = false;//whether has road current in fixing
	public double altruisticDriverRatio = 1;//ratio of altruistic driver among all drivers
	public double truckRatio = 0;//ratio of trucks compares to cars
	public double truckSpeedRatio = 1;//ratio of acceleration and deceleration of truck to car
	public double tickPerSecond = 10;//how many frames per second
	public double frames = 30;//number of frames for one second
	public double vehicleSpace = 10;

	//other data
	public int counter = 0;//a counter of current number of vehicles in the graph
	public double flow = 0;//number of vehicles pass in a certain time
	public Vehicle[][] vehicles = new Vehicle[][]{};//vehicles in screen
        //ArrayList<Vehicle> drawVehicles = new ArrayList(); // move every vehicle objects to this list and refresh every time
           //help to add vehicle from vehicles to drawVehicles
	

        //instantiates
	Driver altruisticDriver;
	Driver egoisticDriver;
	Vehicle car;
	Vehicle truck;
	Vehicle specialVehicle;

	//methods
	//start simulation
	public void start() {
		set();
		run();
	}

	//set settings for drivers, vehicles, and board
	public void set() {
		//initialize attributes
                this.road = new Road(vehicles);
		this.cutInWaitingTimeA = 5;
		this.cutInWaitingTimeE = 1;
		this.hasSpeicalVehicle = false;
		this.specialVehicleFrequency = 0;
		this.laneInFixing = new int[]{};
		this.roadWidth = 50;
		this.roadLength = 1000;
		this.wetness = 1;

		this.width = this.roadWidth * 0.8;
		this.carLength = this.width * 5/2;
		this.truckLength = this.carLength * 2;
		this.maxSpeedA = this.carLength;
		this.maxSpeedE = this.maxSpeedA*1.2;
		this.acceleration = this.carLength *0.2*wetness;
		this.deceleration = this.acceleration * 2;
		this.vehicleSpace = this.carLength * 0.2;



		this.altruisticDriver = new Driver(true, cutInWaitingTimeA, maxSpeedA);
		this.egoisticDriver = new Driver(false, cutInWaitingTimeE, maxSpeedE);
		this.car = new Car(carLength, width, acceleration, deceleration);
		this.truck = new Truck(truckLength, width, acceleration*truckSpeedRatio,deceleration*truckSpeedRatio);
		if (this.hasSpeicalVehicle) {
			this.specialVehicle = new SpecialVehicle(carLength, width, acceleration, deceleration);
		}
	}

	//calculate the positions of cars for next tick from current tick
	public void run() {
		while (true) {
			//set all vehicles to unmoved
			for (int i=0; i<vehicles.length;i++) {
				for (int j=0; j<vehicles[i].length;j++) {
					vehicles[i][j].hasMoved = false;
				}
			}
			//move all vehicles
			move(vehicles);// array out of bound error
                        spawn();
			draw();
			try {
				TimeUnit.MILLISECONDS.sleep((long)(1/this.frames));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	//move vehicles
	public void move(Vehicle[][] lov) {
		int[] n1 = {0,0}, n2 = {1,0}, n3 = {2,0}, n4 = {3,0}, n5 = {4,0};
		while (n1[1] != vehicles[0].length &&
				n2[1] != vehicles[1].length &&
				n3[1] != vehicles[2].length &&
				n4[1] != vehicles[3].length &&
				n4[1] != vehicles[4].length) {
			moveHelper(findMostForward(n1,findMostForward(n2,findMostForward(n3,findMostForward(n4,n5)))));
		}
	}

	//helper function for move()
	public void moveHelper(int[] n) {
		Vehicle a = vehicles[n[0]][n[1]];
		//call helper function if the vehicle is in the most left lane
		if (n[0] == 0) {
			LeftSideHelper(n);
		}
		//call helper function if the vehicle is in the most right lane
		else if (n[0] == this.lanes) {
			RightSideHelper(n);
		}

		//if this vehicle reaches the end of the road
		else if (a.px >= this.roadLength) {
			remove(n);
			counter--;
		}
		else {
			int i = 0;
			while (i<vehicles[n[0]-1].length && vehicles[n[0]-1][i+1].px > a.px) {i++;}
			Vehicle leftFront = vehicles[n[0]-1][i];
			i = 0;
			while (i<vehicles[n[0]+1].length && vehicles[n[0]+1][i+1].px > a.px) {i++;}
			Vehicle rightFront = vehicles[n[0]+1][i];			
			//if this vehicle is the most forward vehicle in the road
			if(n[1]==0) {
				if (a.direction == a.direction.forward) {
					speedUp(a);
					a.px += a.v;
				}
				else if(a.direction == a.direction.left) {
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
				Vehicle front = vehicles[n[0]][n[1]-1];
				//if this vehicle is moving forward
				if (a.direction == a.direction.forward) {
					//if the driver in vehicle a is an egoistic driver and the driver in front vehicle is not 
					if (a.driver.maxSpeed > front.driver.maxSpeed) {
						if (leftFront.px >= rightFront.px) {
							judge(a, front, leftFront, n);
						}
						else {
							judge(a, front, rightFront, n);
						}
					}
					else {
						//judge the situation
						judgeForward(a, front);
					}
				}
				//a is cutting to left
				else if (a.direction == a.direction.left){
					judgeCuttingIn(a,front,leftFront);
					a.px += a.v*0.8;
					a.py += a.v*0.6;
					//if a has completed cutting in
					if (a.py >= leftFront.py) {
						a.py = leftFront.py;
						a.direction = a.direction.forward;
						remove(n);
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
						remove(n);
					}
				}
			}
			a.hasMoved = true;
			n[1]++;
		}
	}

	public void LeftSideHelper(int[] n) {
		Vehicle a = vehicles[n[0]][n[1]];
		//if this vehicle reaches the end of the road
		if (a.px >= this.roadLength) {
			remove(n);
			counter--;
		}
		else {
			int i = 0;
			while (i<vehicles[n[0]+1].length && vehicles[n[0]+1][i+1].px > a.px) {i++;}
			Vehicle rightFront = vehicles[n[0]+1][i];			
			//if this vehicle is the most forward vehicle in the road
			if(n[1]==0) {
				if (a.direction == a.direction.forward) {
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
				Vehicle front = vehicles[n[0]][n[1]-1];
				//if this vehicle is moving forward
				if (a.direction == a.direction.forward) {
					//if the driver in vehicle a is an egoistic driver and the driver in front vehicle is not 
					if (a.driver.maxSpeed > front.driver.maxSpeed) {
						judge(a, front, rightFront, n);
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
						remove(n);
					}
				}
			}
			a.hasMoved = true;
			n[1]++;
		}
	}

	public void RightSideHelper(int[] n) {
		Vehicle a = vehicles[n[0]][n[1]];
		//if this vehicle reaches the end of the road
		if (a.px >= this.roadLength) {
			remove(n);
			counter--;
		}
		else {
			int i = 0;
			while (i<vehicles[n[0]-1].length && vehicles[n[0]-1][i+1].px > a.px) {i++;}
			Vehicle leftFront = vehicles[n[0]-1][i];
			//if this vehicle is the most forward vehicle in the road
			if(n[1]==0) {
				if (a.direction == a.direction.forward) {
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
				Vehicle front = vehicles[n[0]][n[1]-1];
				//if this vehicle is moving forward
				if (a.direction == a.direction.forward) {
					//if the driver in vehicle a is an egoistic driver and the driver in front vehicle is not 
					if (a.driver.maxSpeed > front.driver.maxSpeed) {
							judge(a, front, leftFront, n);
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
						remove(n);
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
	public void judge(Vehicle a, Vehicle front, Vehicle side, int[] n) {
		//a is still far from front Vehicle
		if (a.px + 0.5*a.length < front.px - front.length) {
			catchUp(a, front);
			a.px += a.v;
		}
		else {
			//there is enough space between a and side vehicle
			if (a.px + 0.5*a.length < side.px - side.length) {
				//determine the cut in direction and add a to that Vehicle[]
				if (a.py > side.py) {
					judgeCuttingIn(a,front,side);
					a.direction = a.direction.left;
					a.hasMoved = true;
					int i=0;
					while (a.px < vehicles[n[0]-1][i].px && i<vehicles[n[0]-1].length) {
						i++;
					}
					if (i==vehicles[n[0]-1].length) {
						Vehicle[] temp = vehicles[n[0]-1];
						temp[i] = a;
						vehicles[n[0]-1] = temp;
					}
					else {
						int j=0,k=0;
						Vehicle[] temp = new Vehicle[] {};
						while (j < vehicles[n[0]-1].length + 1) {
							if (j!=i) {
								temp[j++] = vehicles[n[0]-1][k++];
							}
							else {
								temp[j++] = a;
							}
						}
						vehicles[n[0]-1] = temp;
					}
				}
				else {
					a.direction = a.direction.right;
					int i=0;
					while (a.px < vehicles[n[0]+1][i].px && i<vehicles[n[0]+1].length) {
						i++;
					}
					if (i==vehicles[n[0]+1].length) {
						Vehicle[] temp = vehicles[n[0]+1];
						temp[i] = a;
						vehicles[n[0]+1] = temp;
					}
					else {
						int j=0,k=0;
						Vehicle[] temp = new Vehicle[] {};
						while (j < vehicles[n[0]+1].length + 1) {
							if (j!=i) {
								temp[j++] = vehicles[n[0]+1][k++];
							}
							else {
								temp[j++] = a;
							}
						}
						vehicles[n[0]+1] = temp;
					}
				}
			}
			//no chance to cut in
			else {
				judgeForward(a,front);
				a.px += a.v;
			}
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
			if(a.px + 0.5*a.length <= leftFront.px - 0.7*leftFront.length) {

			}
		}
	}

	//relative v between two vehicles
	public double relativeSpeed(Vehicle a, Vehicle b) {
		return a.v-b.v;
	}

	//find the most forward vehicle
	public int[] findMostForward(int[] n1,int[] n2) {
		//if two vehicles are the same
		if (vehicles[n1[0]][n1[1]].equals(vehicles[n2[0]][n2[1]])) {
			//if vehicle direction is left
			if (vehicles[n1[0]][n1[1]].direction == vehicles[n1[0]][n1[1]].direction.left) {
				return n2;
			}
			else return n1;
		}
		else if (vehicles[n1[0]][n1[1]].hasMoved) {
			return n2;
		}
		else if (vehicles[n1[0]][n1[1]].px>=vehicles[n2[0]][n2[1]].px) {
			return n1;
		}
		return n2;
	}

	//remove a vehicle from the list of vehicles
	public void remove(int[] n) {
		int i=0,j=0;
		Vehicle[] temp = new Vehicle[] {};
		while (j<vehicles[n[0]].length) {
			if (j != n[1]) {
				temp[i++] = vehicles[n[0]][n[j++]];
			}
			else {j++;}
		}
		vehicles[n[0]] = temp;
	}

	//Whether there is special vehicle on road
	public boolean hasSpecialVehicle(Vehicle[] vehicles)  {
		for (int i = 0; i < vehicles.length; i++) {
			if (vehicles[i].getClass() == this.specialVehicle.getClass()) {
				return true;
			}
		}
		return false;
	}

	//draw current tick
	public void draw() {
                frame.setSize(roadLength,lanes*roadWidth);
                frame.setLayout(new BorderLayout());
                frame.add(road,BorderLayout.CENTER);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
               
                
	}

	//spawn a new vehicle
	public void spawn() {
		Random a = new Random();
		double vehicleType = a.nextDouble();
		double driverType = a.nextDouble();
		Driver newDriver;
		Vehicle newVehicle;
		//decide which kind of driver is in the vehicle
		if (driverType <= this.altruisticDriverRatio) {
			newDriver = this.altruisticDriver;
		}
		else {
			newDriver = this.egoisticDriver;
		}
		//decide which kind of vehicle should be spawned
		if (vehicleType < this.truckRatio) {
			newVehicle = this.truck;}
		else {
			newVehicle = this.car;
		}
		newVehicle.driver = newDriver;
		//spawn new vehicle in a random possible lane
		int[] l = new int[] {0,1,2,3,4};
		while (l != null) {
			int i = a.nextInt(l.length);
			Vehicle lastVehicle = vehicles[l[i]][vehicles[l[i]].length-1];
			if (newVehicle.px + 0.5*newVehicle.length < lastVehicle.px - 0.5*lastVehicle.length) {
				newVehicle.py = (l[i]*2+1)*this.roadWidth/(this.lanes*2-1);
				vehicles[l[i]][vehicles[l[i]].length] = newVehicle;
				counter++;
				break;
			}
			else {
				int i1 = 0, i2 = 0;
				int[] temp = new int[] {};
				while (i1<l.length) {
					if (i2+1!=i) {
						temp[i1++] = l[i2];
					}
					i2++;
				}
				l = temp;
			}
		}
	}
        
        public static void main(String[] args) {
		// TODO Auto-generated method stub
		TrafficSimulation a = new TrafficSimulation();
		a.set();
                a.draw();
                a.run();
	}
}
