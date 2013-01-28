package lab2;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;

/* 
 * OdometryCorrection.java
 */

public class OdometryCorrection extends Thread {
	private static final long CORRECTION_PERIOD = 10;
	private Odometer odometer;
	private static final SensorPort lightPort = SensorPort.S2;
	private LightSensor ls = new LightSensor(lightPort, false);

	// constructor
	public OdometryCorrection(Odometer odometer) {
		this.odometer = odometer;
	}

	// run method (required for Thread)
	public void run() {
		long correctionStart, correctionEnd;
		
		ls.setFloodlight(true);
		boolean isLine = false, firstLeg = true;
		double temp1 = 0.0, temp2 = 0.0;
		int lastLight = 0;
		//turn the light on, and initialize variables
		
		while (true) {
			correctionStart = System.currentTimeMillis();
			// TODO: put your correction code here

			//LCD.drawString("LV: " + ls.getNormalizedLightValue(), 0, 4);
			
			/*
			//Threshold filtering -- Simple but works under given conditions
			if(ls.getNormalizedLightValue() < 400) isLine = true;
			else isLine = false;
			*/
			
			//Derivative filtering -- A little more complex, and will work under more varying conditions
			if(lastLight == 0) lastLight = ls.getNormalizedLightValue();
			else temp1 = ls.getNormalizedLightValue() - lastLight;
			
			if(Math.abs(temp1) > 4) isLine = true;
			else isLine = false;
			lastLight = ls.getNormalizedLightValue();
			
			
			//Printing some information
			LCD.drawString("iL:" + isLine + " tp:" + temp1, 0, 5);
			
			if(isLine) {
				temp2 = this.odometer.getTheta()  % 180;
				if(temp2 > 100) firstLeg = false;
				LCD.drawString("fslg: " + firstLeg, 0, 4);
				//temp2 is used to determine direction and firstLeg
				
				//Sound.beep();
				
				/*
				 * Depending on the value of the Theta, the robot will decide which direction it is moving
				 * Then, depending on the value of the odometer for that direction, it will decide which line it is crossing, the first or second
				 * Lastly, it sets the location to the location of the line, plus or minus the distance from the sensor to the center of the robot,
				 * depending on which direction the robot is moving, toward the origin, or away from it.
				 * This last value is indicated by the boolean, firstLeg, which determines whether the robot is on the first half of it's journey, or the second
				 */
				
				if(Math.abs(temp2) < 5 || Math.abs(temp2 - 180) < 5) { //Movement in the Y direction
					if(this.odometer.getY() > 1 && this.odometer.getY() < 30) {
						this.odometer.setY(15 + (firstLeg ? -Constants.SENSOR_DISTANCE : Constants.SENSOR_DISTANCE));
					}
					else if(this.odometer.getY() > 30 && this.odometer.getY() < 65){
						this.odometer.setY(45 + (firstLeg ? -Constants.SENSOR_DISTANCE : Constants.SENSOR_DISTANCE));
					}
					
					LCD.drawString("Correcting Y", 0, 6);
					Sound.playTone(66, 1, 60);
					//Notifications for a successful correction
				}
				else if(Math.abs(temp2 - 90) < 5) { //Movement in the X direction
					if(this.odometer.getX() < 30) {
						this.odometer.setX(15 + (firstLeg ? -Constants.SENSOR_DISTANCE : Constants.SENSOR_DISTANCE));
					}
					else {
						this.odometer.setX(45 + (firstLeg ? -Constants.SENSOR_DISTANCE : Constants.SENSOR_DISTANCE));
					}
					
					LCD.drawString("Correcting X", 0, 6);
					Sound.playTone(88, 1, 60);
					//Notifications for a successful correction

				}
				else { //Not moving explicitly in either direction
					//do nothing, no correction
					
					LCD.drawString("Not Correcting", 0, 6);
				}
							
			}
			else LCD.drawString("Not Correcting", 0, 6);
			
			
				

			
			
			// this ensure the odometry correction occurs only once every period
			correctionEnd = System.currentTimeMillis();
			if (correctionEnd - correctionStart < CORRECTION_PERIOD) {
				try {
					Thread.sleep(CORRECTION_PERIOD
							- (correctionEnd - correctionStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometry correction will be
					// interrupted by another thread
				}
			}
		}
	}
}