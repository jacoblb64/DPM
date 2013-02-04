package lab3;

/*
 * SquareDriver.java
 */

import java.util.Queue;

import lab2.Constants;
import lab2.Odometer;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public class Driver {
	private static final int FORWARD_SPEED = 250;
	private static final int ROTATE_SPEED = 150;
	
	private static WayPoint curPoint = new WayPoint(0,0);
	private static Queue<WayPoint> wayPoints = new Queue<WayPoint>();
	private static Odometer odometer;

	public static void drive(int path, Odometer od) {
		// reset the motors
		for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { Motor.A, Motor.C }) {
			motor.stop();
			motor.setAcceleration(3000);
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
	
	static void travelTo(WayPoint point) {		
		WayPoint curPoint = new WayPoint(odometer.getX(), odometer.getY());
		double angle = curPoint.getAngle(point, odometer.getTheta());
		
		
		LCD.drawString("dist: " + curPoint.getDistance(point), 0, 4);
		LCD.drawString("head: " + curPoint.getHeading(point), 0, 5);
		LCD.drawString("ang: " + angle, 0, 6);
		
		
		turnTo(angle);
		
		Motor.A.setSpeed(FORWARD_SPEED);
		Motor.C.setSpeed(FORWARD_SPEED);

		Motor.A.rotate(convertDistance(Constants.WHEEL_RADIUS, curPoint.getDistance(point)), true);
		Motor.C.rotate(convertDistance(Constants.WHEEL_RADIUS, curPoint.getDistance(point)), false);
	}
	
	static void turnTo(double theta) {
		
		Motor.A.setSpeed(ROTATE_SPEED);
		Motor.C.setSpeed(ROTATE_SPEED);

		Motor.A.rotate(convertAngle(Constants.WHEEL_RADIUS, Constants.ROBOT_WIDTH, theta), true);
		Motor.C.rotate(-convertAngle(Constants.WHEEL_RADIUS, Constants.ROBOT_WIDTH, theta), false);
	}

	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
}