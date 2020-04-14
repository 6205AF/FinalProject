import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TrafficThread extends Thread{

    private JFrame jFrame;
    private boolean flag = true;
    public static int refresh = 500;
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
        if (flag){
            //Draw the road situation
            trafficSimulation.road.removeAll();
//            for (int i = 0; i < TrafficSimulation.lanes ; i ++){
//                for (Vehicle v : trafficSimulation.vehicles.get(i)) {
//                    trafficSimulation.road.addVehicle(v);
//                }
//            }
            Car testCar = new Car(trafficSimulation.carLength, trafficSimulation.width, 0, 0, 2) {
            };
            trafficSimulation.road.addVehicle(testCar);
            jFrame.repaint();
            jFrame.add(trafficSimulation.road, BorderLayout.CENTER);
            jFrame.setVisible(true);
            try {
                TrafficThread.sleep(refresh);
                //move
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
