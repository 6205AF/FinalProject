import sun.tools.jps.Jps;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TrafficThread extends Thread{

    private JFrame jFrame;
    private boolean flag = true;
    public static int refresh = 500; // refresh time unit
    public static int refreshTimes = 20;
    private TrafficSimulation trafficSimulation;


    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public JFrame getjFrame() {
        return jFrame;
    }

    public void setjFrame(JFrame jFrame) {
        this.jFrame = jFrame;
    }

    public TrafficThread(){}

    public TrafficThread(JFrame jFrame, TrafficSimulation trafficSimulation){
        this.jFrame = jFrame;
        this.trafficSimulation = trafficSimulation;
    }

    @Override
    public void run() {
        //run the simulation with Move method
        HashMap<Integer,ArrayList<Vehicle>> vehicles = trafficSimulation.vehicles;
//        while (flag){
        for (int i = 0; i < refreshTimes ; i++){
            //Draw the road situation
            JPanel jPanel = trafficSimulation.road;
            jPanel.repaint();
            jFrame.add(jPanel,BorderLayout.CENTER);
            jFrame.setVisible(true);
            // move for next tick
            for (ArrayList<Vehicle> vs: vehicles.values()){
                for (Vehicle v : vs){
                    v.px += 30;
                }
            }
            try {
                TrafficThread.sleep(refresh);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
