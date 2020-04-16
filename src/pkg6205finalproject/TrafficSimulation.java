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
	public static int laneInFixing;//the numbers of lane is in fixing
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
	public HashMap<Integer,ArrayList<Vehicle>> vehicles;
	public Move move;

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
		this.road = new Road(this);
		this.laneInFixing = 4;
		// Initialize vehicles of lanes
		//--------CHECK-------
		System.out.println("initialize null ");
		move = new Move();
		vehicles = new HashMap<Integer,ArrayList<Vehicle>>();
		for (Integer i = 0; i < lanes; i++){
			vehicles.put(i,new ArrayList<Vehicle>());
			System.out.println(vehicles.get(i));
		}

//		spawn();
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
		JPanel panelSouth = new JPanel();
		panelSouth.add(start);
		panelSouth.add(stop);
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
	}

	// --------------------CHECK-----------------------    
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