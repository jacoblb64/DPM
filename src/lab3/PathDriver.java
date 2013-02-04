package lab3;

import java.util.Queue;

import lab2.Constants;
import lab2.Odometer;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;

public class PathDriver extends Thread{
	
	private static final long SLEEP_PERIOD = 200;
	private static final int FORWARD_SPEED = 250;
	private static final int ROTATE_SPEED = 50;
	
	private WayPoint curPoint = new WayPoint(0,0);
	private double curTheta = 0;
	private Queue<WayPoint> wayPoints = new Queue<WayPoint>();
	private Odometer odometer;
	private Object lock;
	
	private NXTRegulatedMotor leftMotor = Motor.A;
	private NXTRegulatedMotor rightMotor = Motor.C;
	
	public PathDriver(int path, Odometer odometer) throws Exception{
		
		LCD.drawString("I'm alive!", 0, 4);

		
		if (path != 1 && path != 2) throw new Exception();
		
		if (path == 1) {
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
		
		// resetting motors
		for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { Motor.A, Motor.C }) {
			motor.stop();
			motor.setAcceleration(3000);
		}
		
		LCD.drawString("I'm alive!", 0, 4);

		
	}
	
	public void run() {
		long displayStart, displayEnd;
		
		
		while (true) {
			displayStart = System.currentTimeMillis();
		

			
			travelTo((WayPoint) wayPoints.pop());
			
			
			
			
			
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
	
	/*
	void travelTo(WayPoint point) {
		LCD.drawString(point.toString(), 0, 4);
		
		do { // while (curPoint.getForwardError(point, curTheta) > 1);
			
			this.curPoint.setX(this.odometer.getX());
			this.curPoint.setY(this.odometer.getY());
			this.curTheta = this.odometer.getTheta();
			
			
			LCD.drawString("GH: " + this.curPoint.getHeading(point), 0, 5);
			LCD.drawString("GAE: " + this.curPoint.getAngularError(this.curPoint.getHeading(point), curTheta), 0, 6);
			LCD.drawString("GFE: " + this.curPoint.getForwardError(point, curTheta), 0, 7);
			
			if (Math.abs(this.curPoint.getAngularError(this.curPoint.getHeading(point), curTheta)) > 5) {
				turnTo(this.curPoint.getHeading(point));
			}
			else {
				leftMotor.setSpeed(FORWARD_SPEED);
				rightMotor.setSpeed(FORWARD_SPEED);
				
				leftMotor.forward();
				rightMotor.forward();
			}		
		
		} while (Math.abs(curPoint.getForwardError(point, curTheta)) > 5);
		
		leftMotor.stop();
		rightMotor.stop();
	}
	
	void turnTo(double theta) {
		leftMotor.stop();
		rightMotor.stop();
		
		
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		
		if (this.curPoint.getAngularError(theta, curTheta)  < 0) {
			leftMotor.rotate(convertAngle(Constants.WHEEL_RADIUS, Constants.ROBOT_WIDTH, theta), true);
			rightMotor.rotate(-convertAngle(Constants.WHEEL_RADIUS, Constants.ROBOT_WIDTH, theta), false);
		}
		else {
			rightMotor.rotate(convertAngle(Constants.WHEEL_RADIUS, Constants.ROBOT_WIDTH, theta), true);
			leftMotor.rotate(-convertAngle(Constants.WHEEL_RADIUS, Constants.ROBOT_WIDTH, theta), false);
		}
	}
	*/

	void travelTo(WayPoint point) {
		
		double distance = convertDistance(Constants.WHEEL_RADIUS, curPoint.getDistance(point));
		
		leftMotor.rotate((int) distance, true);
		rightMotor.rotate((int) distance, true);
	}
	
	boolean isNavigating() {
		
		return false;
	}
	
	// taken from SquareDriver from lab2
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	//
}
