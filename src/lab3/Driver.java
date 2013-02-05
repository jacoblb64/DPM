package lab3;

/*
 * add
 */

import java.util.Queue;

import lab1.UltrasonicPoller;
import lab2.Constants;
import lab2.Odometer;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class Driver {
	private static final int FORWARD_SPEED = 250;
	private static final int ROTATE_SPEED = 150;
	
	private static WayPoint curPoint = new WayPoint(0,0);
	private static Queue<WayPoint> wayPoints = new Queue<WayPoint>();
	private static Odometer odometer;
	private static boolean isNavigating;
	private static SensorPort usPort;
	private static UltrasonicSensor usSensor;
	private static boolean avoiding = false;

	public static void drive(int path, Odometer od) {
		// reset the motors
		for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { Motor.A, Motor.C }) {
			motor.stop();
			motor.setAcceleration(2000);
		}
		
		odometer = od;
		
		if (path != 1 && path != 2) {
			LCD.clear();
			LCD.drawString("invalid path", 0, 1);
			return;	
		}
		
		if (path == 1) {
			wayPoints.push(new WayPoint(60,30));
			wayPoints.push(new WayPoint(30, 30));
			wayPoints.push(new WayPoint(30, 60));
			wayPoints.push(new WayPoint(60, 0));
		}
		else {
			wayPoints.push(new WayPoint(0, 60));
			wayPoints.push(new WayPoint(60, 0));
			
			usPort = SensorPort.S1;
			usSensor = new UltrasonicSensor(usPort);
			avoiding = true;
		}

		// wait 5 seconds
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// there is nothing to be done here because it is not expected that
			// the odometer will be interrupted by another thread
		}

		//for (int i = 0; i < 4; i++) {
		while (!wayPoints.isEmpty()) {
						
			travelTo((WayPoint) wayPoints.pop());

		}
	}
	
	public static void travelTo(WayPoint point) {
		isNavigating = true;
		LCD.drawString(point.toString(), 0, 3);
		
		curPoint = new WayPoint(odometer.getX(), odometer.getY());
		double angle = curPoint.getAngle(point, odometer.getTheta());
		
		
		LCD.drawString("dist: " + curPoint.getDistance(point), 0, 4);
		LCD.drawString("head: " + curPoint.getHeading(point), 0, 5);
		LCD.drawString("ang: " + angle, 0, 6);
		
		
		turnTo(angle);
			
		Motor.A.setSpeed(FORWARD_SPEED);
		Motor.C.setSpeed(FORWARD_SPEED);
				
		if(avoiding) {
			WayPoint start = curPoint;
				
			
			while (curPoint.getDistance(point) > 3) {
				
				Motor.A.forward();
				Motor.C.forward();
				
				LCD.drawString("d:" + usSensor.getDistance(), 10, 3);

				
				if (usSensor.getDistance() < 20) {
					wayPoints.push(new WayPoint(odometer.getX() + 19*Math.sqrt(2), odometer.getY() + 19*Math.sqrt(2)));
					wayPoints.push(new WayPoint(odometer.getX() + 45, odometer.getY()));

					
					wayPoints.push(point);
					
					isNavigating = false;
					return;
				}
				

				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				curPoint.setX(odometer.getX());
				curPoint.setY(odometer.getY());
			}
			Motor.A.stop();
			Motor.C.stop();
		}
		else {
			Motor.A.forward();
			Motor.C.forward();
			
			
			Motor.A.rotate(convertDistance(Constants.WHEEL_RADIUS, curPoint.getDistance(point)), true);
			Motor.C.rotate(convertDistance(Constants.WHEEL_RADIUS, curPoint.getDistance(point)), false);
		}
		
		isNavigating = false;
	}
	
	public static void turnTo(double theta) {
		
		Motor.A.setSpeed(ROTATE_SPEED);
		Motor.C.setSpeed(ROTATE_SPEED);

		Motor.A.rotate(convertAngle(Constants.WHEEL_RADIUS, Constants.ROBOT_WIDTH, theta), true);
		Motor.C.rotate(-convertAngle(Constants.WHEEL_RADIUS, Constants.ROBOT_WIDTH, theta), false);
	}
	
	public static boolean isNavigating() {
		return isNavigating;
	}

	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
}