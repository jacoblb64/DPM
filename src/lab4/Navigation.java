package lab4;

import lab2.Constants;
import lejos.nxt.Motor;


public class Navigation {
	// put your navigation code here 
	
	private Odometer odo;
	private TwoWheeledRobot robot;
	
	final static int FAST = 5, SLOW = 15;
	final static double DEG_ERR = 3.0, CM_ERR = 1.0;
	
	public Navigation(Odometer odo) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
	}
	
	public void travelTo(double x, double y) {
		// USE THE FUNCTIONS setForwardSpeed and setRotationalSpeed from TwoWheeledRobot!
		
		double minAng;
		while (Math.abs(x - odo.getX()) > CM_ERR || Math.abs(y - odo.getY()) > CM_ERR) {
			minAng = (Math.atan2(y - odo.getY(), x - odo.getX())) * (180.0 / Math.PI);
			if (minAng < 0)
				minAng += 360.0;
			this.turnTo(minAng, false);
			this.robot.setSpeeds(FAST, 0);
		}
		this.robot.setSpeeds(0, 0);
		
	}
	
	public void travelToStraight(double x, double y) {
		
		
		double delX = x - odo.getX();
		double delY = y - odo.getY();
		double distance = Math.sqrt(delX*delX + delY*delY);
		
		//robot.setForwardSpeed(FAST);
		
		Motor.A.setSpeed(100);
		Motor.C.setSpeed(100);
		
		Motor.A.rotate(robot.convertDistance(Constants.WHEEL_RADIUS, distance), true);
		Motor.C.rotate(robot.convertDistance(Constants.WHEEL_RADIUS, distance), false);
	}
	
	public void travelStraight(double distance) {
		
		Motor.A.setSpeed(100);
		Motor.C.setSpeed(100);
		
		Motor.A.rotate(robot.convertDistance(Constants.WHEEL_RADIUS, distance), true);
		Motor.C.rotate(robot.convertDistance(Constants.WHEEL_RADIUS, distance), false);
	}
	
	public void travelToSimple(double x, double y) {
		turnToSimple(getHeading(x, y));	
		travelToStraight(x, y);
	}
	
	private double getHeading(double x, double y) {
		double currentX = odo.getX();
		double currentY = odo.getY();
		double thetaDegrees = odo.getAng();
		
		// Calculate relative distance
		double relativeX = x - currentX, relativeY = y - currentY;
		
		// Get heading
		double heading = Math.toDegrees(Math.atan2(relativeX,relativeY));
		//LCD.drawString("H: " + heading, 0, 4);
		
		// Ensure heading is between 0 and 360
		if (heading < 0)
			heading += 360; 
		
		return heading; 
	}
	
	public void turnToSimple(double theta) {	
		// Robot's current angle, in degrees	
		double thetaDegrees = odo.getAng();
		
		// Return value
		double deltaTheta = 0;

		// Ensure theta is between 0 and 360
		if (theta < 0)
			theta += 360; 
		
		// Get the raw difference
		double rawDif = (theta - thetaDegrees); 
		
		// Calculation from tutorial slides
		if (Math.abs(rawDif) <= 180) {
			deltaTheta = rawDif; 
		}
		else if (rawDif < -180) {
			deltaTheta = rawDif + 360;
		}
		else if (rawDif > 180) {
			deltaTheta = rawDif - 360;
		}
		
		// Rotate 
		turnSimple(deltaTheta);
	}
	
	public void turnSimple(double theta) {
		
		Motor.A.setSpeed(50);
		Motor.C.setSpeed(50);

		Motor.A.rotate(robot.convertAngle(Constants.WHEEL_RADIUS, Constants.ROBOT_WIDTH, theta), true);
		Motor.C.rotate(-robot.convertAngle(Constants.WHEEL_RADIUS, Constants.ROBOT_WIDTH, theta), false);
	}
	
	public void turnTo(double angle, boolean stop) {
		// USE THE FUNCTIONS setForwardSpeed and setRotationalSpeed from TwoWheeledRobot!
		
		double error = angle - this.odo.getAng();

		while (Math.abs(error) > DEG_ERR) {

			error = angle - this.odo.getAng();

			if (error < -180.0) {
				this.robot.setRotationSpeed(SLOW);
			} else if (error < 0.0) {
				this.robot.setRotationSpeed(-SLOW);
			} else if (error > 180.0) {
				this.robot.setRotationSpeed(-SLOW);
			} else {
				this.robot.setRotationSpeed(SLOW);
			}
		}

		if (stop) {
			this.robot.setSpeeds(0, 0);
		}
	}
	
	public void goForward(double distance) {
		this.travelTo(Math.cos(Math.toRadians(this.odo.getAng())) * distance, Math.cos(Math.toRadians(this.odo.getAng())) * distance);

	}
	
}
