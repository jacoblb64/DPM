package lab3;

import java.util.Queue;
import lab2.Odometer;
import lejos.nxt.Sound;

public class PathDriver extends Thread{
	
	private static final long SLEEP_PERIOD = 200;
	
	private WayPoint curPoint = new WayPoint(0,0);
	private double curTheta = 0;
	private Queue<WayPoint> wayPoints = new Queue<WayPoint>();
	private Odometer odometer;
	
	public PathDriver(int path, Odometer odometer) throws Exception{
		if(path != 1 || path != 2) throw new Exception();
		
		if(path == 1) {
			wayPoints.push(new WayPoint(60,30));
			wayPoints.push(new WayPoint(30, 30));
			wayPoints.push(new WayPoint(30, 60));
			wayPoints.push(new WayPoint(60, 0));
		}
		else {
			wayPoints.push(new WayPoint(0, 60));
			wayPoints.push(new WayPoint(60, 0));
		}
		
		this.odometer = odometer;
	}
	
	public void run() {
		long displayStart, displayEnd;
		
		while (true) {
			displayStart = System.currentTimeMillis();
		
			this.curPoint.setX(this.odometer.getX());
			this.curPoint.setY(this.odometer.getY());
			this.curTheta = this.odometer.getTheta();
			
			
			travelTo(curPoint);
			
			
			
			
			
			
			displayEnd = System.currentTimeMillis();
			if (displayEnd - displayStart < SLEEP_PERIOD) {
				try {
					Thread.sleep(SLEEP_PERIOD - (displayEnd - displayStart));
				} catch (InterruptedException e) {
					Sound.beepSequence();
					Sound.beepSequenceUp();
					/*
					 *  this is not expected to happen, but in the case that it does,
					 *  have the robot beep a unique pattern to signal something has gone wrong
					 */
				}
			}
		}
	}
	
	void travelTo(WayPoint point) {
		
	}
	
	void turnTo(double theta) {
		
	}
	
	boolean isNavigating() {
		
		return false;
	}
}
