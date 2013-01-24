package lab2;

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
	}

	// run method (required for Thread)
	public void run() {
		long updateStart, updateEnd;

		double[] sums = new double[3]; // vector of moving sums, for (x, y, theta)
		//treating initial position as (0, 0, 0)
		
		double lambda = 0.0, rho = 0.0, delTheta, delC, temp;
		
		Motor.A.resetTachoCount(); Motor.C.resetTachoCount(); //resetting tacho counts
		
		while (true) {
			updateStart = System.currentTimeMillis();
			// TODO: put (some of) your odometer code here
			
			temp = ((rho - Motor.C.getTachoCount()) - (lambda - Motor.A.getTachoCount()) ) * (Constants.WHEEL_RADIUS);
			delTheta = temp / Constants.ROBOT_WIDTH;
			delC = temp / 2;
			rho = Motor.C.getTachoCount(); lambda = Motor.A.getTachoCount();
			
			
			sums[2] += delTheta;
			temp = sums[2] + delTheta / 2;
			sums[1] += delC * Math.sin(temp);
			sums[0] += delC * Math.cos(temp);

			synchronized (lock) {
				// don't use the variables x, y, or theta anywhere but here!
				this.x = sums[0];
				this.y = sums[1];
				this.theta = sums[2];
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