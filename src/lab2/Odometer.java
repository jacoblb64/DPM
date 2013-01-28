package lab2;

import lejos.nxt.LCD;
import lejos.nxt.Motor;

/*
 * Odometer.java
 */

public class Odometer extends Thread {
	// robot position
	private double x, y, theta;

	// odometer update period, in ms
	private static final long ODOMETER_PERIOD = 25;

	// lock object for mutual exclusion
	private Object lock;

	// default constructor
	public Odometer() {
		x = 0.0;
		y = 0.0;
		theta = 0.0;
		lock = new Object();
		
		Motor.A.resetTachoCount(); Motor.C.resetTachoCount();
		//resetting tacho counts, this is done in the constructor to avoid locking the wheels while running
	}

	// run method (required for Thread)
	public void run() {
		long updateStart, updateEnd;
		
		double thetaRadians = 0.0, lambda = 0.0, rho = 0.0, delTheta, delC, temp1, temp2;
		//initializing variables
		
		
		while (true) {
			updateStart = System.currentTimeMillis();
			// TODO: put (some of) your odometer code here
					
			temp1 = rho - Motor.C.getTachoCount() * Constants.RadDeg_Convert; temp2 = lambda - Motor.A.getTachoCount() * Constants.RadDeg_Convert;
			
			delTheta = (temp1 - temp2 ) * (Constants.WHEEL_RADIUS / Constants.ROBOT_WIDTH);
			delC = (temp1 + temp2 ) * (Constants.WHEEL_RADIUS / 2);
			rho = Motor.C.getTachoCount() * Constants.RadDeg_Convert; lambda = Motor.A.getTachoCount() * Constants.RadDeg_Convert;
			
			/*
			 * Calculations as outlined in the Odometry Tutorial slides on myCourses
			 * Using the tachometer counts from the wheels, the robot's dimensions, and basic geometry and trigonometry to calculate intermediate values.
			 * These values are then used to extrapolate values of x, y, and theta, which are treated as moving sums in this case.
			 * It should be noted that implemented here is an alternative theta whose value is in radians, thetaRadians, for calculations,
			 * this way, the "proper" theta displayed and kept is in degrees.
			 * 
			 * Similar operations on rho and lambda are consolidated onto single lines for readability
			 */

			synchronized (lock) {
				// don't use the variables x, y, or theta anywhere but here!
				
				thetaRadians += delTheta;
				temp1 = thetaRadians + delTheta / 2;
				this.theta = thetaRadians / Constants.RadDeg_Convert;
				this.y += -delC * Math.cos(temp1);
				this.x += -delC * Math.sin(temp1);
				
				/*
				 * Math used to extrapolate x, y, and theta from the above calculations.
				 * 
				 * Note:
				 * Defining forward and right to be positive Y and X directions respectively,
				 * and clockwise from the Y axis as positive Theta, as outlined in the lab specifications
				 */
			}

			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < ODOMETER_PERIOD) {
				try {
					Thread.sleep(ODOMETER_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometer will be interrupted by
					// another thread
				}
			}
		}
	}

	// accessors
	public void getPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				position[0] = x;
			if (update[1])
				position[1] = y;
			if (update[2])
				position[2] = theta;
		}
	}

	public double getX() {
		double result;

		synchronized (lock) {
			result = x;
		}

		return result;
	}

	public double getY() {
		double result;

		synchronized (lock) {
			result = y;
		}

		return result;
	}

	public double getTheta() {
		double result;

		synchronized (lock) {
			result = theta;
		}

		return result;
	}

	// mutators
	public void setPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				x = position[0];
			if (update[1])
				y = position[1];
			if (update[2])
				theta = position[2];
		}
	}

	public void setX(double x) {
		synchronized (lock) {
			this.x = x;
		}
	}

	public void setY(double y) {
		synchronized (lock) {
			this.y = y;
		}
	}

	public void setTheta(double theta) {
		synchronized (lock) {
			this.theta = theta;
		}
	}
}