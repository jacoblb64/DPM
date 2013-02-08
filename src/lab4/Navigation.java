package lab4;

import lejos.nxt.LCD;


public class Navigation {
	// put your navigation code here 
	
	private Odometer odo;
	private TwoWheeledRobot robot;
	
	final static int FAST = 200, SLOW = 100, ACCELERATION = 4000;
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
			this.robot.setForwardSpeed(FAST);
		}
		this.robot.setSpeeds(0, 0);
		
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
