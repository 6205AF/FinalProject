
import sun.tools.jps.Jps;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TrafficSimulation {

	//settings of road
	public Road road;
	public static int lanes = 5;//number of lanes of the road
	public static int roadWidth = lanes * Road.LANE_WIDTH;//the width in pixels for each road;
	public static int roadLength = Road.LANE_LENGTH;//the length in pixels for roads
	public static double wetness = 1;//a ratio influences the speed, acceleration and deceleration of vehicles
	public static int[] laneInFixing;//the numbers of lane is in fixing
	public double speedLimit;//speed limit of the road

	//settings of vehicles
	public static double width =  Road.LANE_WIDTH * 0.8;//vehicle width
	public static double carLength = width * 2.5;//length of car and special vehicles
	public static double truckLength = carLength * 2;//length of truck
	public static double acceleration = carLength * 0.2 * wetness;//maximum Vehicle acceleration per tick
	public static double deceleration = acceleration * 2;//maximum Vehicle deceleration per tick

	//settings for drivers
	public static double maxSpeedA = carLength;//the maximum speed could reach for altruistic driver
	public static int cutInWaitingTimeA = 5;//start cutting in for x ticks after switch turn signal on for altruistic driver
	public static double maxSpeedE = maxSpeedA * 1.2;//the maximum speed could reach for egoistic driver
	public static int cutInWaitingTimeE = 1;//start cutting in for x ticks after switch turn signal on for egoistic driver
	public static int specialVehicleFrequency = 0;//special vehicles appear once every x ticks;

	//other settings
	public boolean hasSpeicalVehicle = false;//whether spawn special vehicle
	public boolean hasSpeicalTruck = false;//whether spawn special vehicle
	public boolean hasRoadInFixing = false;//whether has road current in fixing
	public double altruisticDriverRatio = 1;//ratio of altruistic driver among all drivers
	public double truckRatio = 0;//ratio of trucks compares to cars
	public double truckSpeedRatio = 1;//ratio of acceleration and deceleration of truck to car
	public double tickPerSecond = 10;//how many frames per second
	public double frames = 30;//number of frames for one second
	public double vehicleSpace = carLength * 0.2;

	//other data
	public int counter = 0;//a counter of current number of vehicles in the graph
	public double flow = 0;//number of vehicles pass in a certain time

	//public Vehicle[][] vehicles = new Vehicle[][]{};//vehicles in screen
	public HashMap<Integer,ArrayList<Vehicle>> vehicles = new HashMap<Integer,ArrayList<Vehicle>>();

	//instantiates
	Driver altruisticDriver;
	Driver egoisticDriver;
	Vehicle car;
	Vehicle truck;
	Vehicle specialVehicle;

	//set settings for drivers, vehicles, and board
	public TrafficSimulation() {
		boolean running = false;
		//initialize attributes
		this.road = new Road(vehicles);
		this.laneInFixing = new int[]{};
		this.altruisticDriver = new Driver(true, cutInWaitingTimeA, maxSpeedA);
		this.egoisticDriver = new Driver(false, cutInWaitingTimeE, maxSpeedE);
		// ----------------special vehicle frequency should also be considered--------------------
		if (this.hasSpeicalVehicle) {
			this.specialVehicle = new SpecialVehicle(carLength, width, acceleration, deceleration);
		}
		// initilize vehicles of lanes
		for (int i = 0; i < lanes; i++){
			vehicles.put(i,new ArrayList<Vehicle>());
		}

	}

	//calculate the positions of cars for next tick from current tick
	public void run() {
		TrafficSimulation simulation = this;
		//graphs
		JFrame frame = new JFrame("Traffic Simulation");
		frame.setSize((int)Road.LANE_LENGTH,(int)lanes*Road.LANE_WIDTH+100);
		frame.setResizable(true);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		//south: button : start, stop, add car
		JButton start = new JButton("Start");
		JButton stop = new JButton("Stop");
		JButton addcar = new JButton("Add Car");
		JPanel panelSouth = new JPanel();
		panelSouth.add(start);
		panelSouth.add(stop);
		panelSouth.add(addcar);
		panelSouth.setVisible(true);
		frame.add(panelSouth,BorderLayout.SOUTH);
		frame.setVisible(true);
		TrafficThread trafficThread = new TrafficThread(frame,simulation);
		//start button event listerner
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				trafficThread.start();
			}
		});
		//stop button event listener
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				trafficThread.stop();
			}
		});

		//add car button event listener
		addcar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// --------------CHECK---------------
				simulation.spawn();
			}
		});
	}

	// --------------------CHECK-----------------------
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
		ArrayList<Integer> l = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4));
		while (l != null) {
			int i = a.nextInt(l.size());
			if (vehicles.get(l.get(i)).size()==0) {
				vehicles.get(l.get(i)).add(newVehicle);
				counter++;
				break;
			}
			Vehicle lastVehicle = vehicles.get(l.get(i)).get(vehicles.get(l.get(i)).size()-1);
			if (newVehicle.px + 0.5*newVehicle.length < lastVehicle.px - 0.5*lastVehicle.length) {
				newVehicle.py = (l.get(i)*2+1)*this.roadWidth/(this.lanes*2-1);
				vehicles.get(l.get(i)).add(newVehicle);
				counter++;
				break;
			}
			else {
				l.remove(i);
			}
		}
	}
        
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TrafficSimulation trafficSimulation = new TrafficSimulation();
		try {
			trafficSimulation.run();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}