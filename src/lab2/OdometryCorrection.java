package lab2;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

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
		double temp;
		
		while (true) {
			correctionStart = System.currentTimeMillis();
			// TODO: put your correction code here

			//LCD.drawString("LV: " + ls.getNormalizedLightValue(), 0, 4);
			
			if(ls.getNormalizedLightValue() < 400) isLine = true;
			else isLine = false;
			
			LCD.drawString("isL:" + isLine, 0, 5);
			
			if(isLine) {
				temp = this.odometer.getTheta()  % 180;
				if(temp > 100) firstLeg = false;
				LCD.drawString("fslg: " + firstLeg, 0, 4);
				
				if(Math.abs(temp) < 5) { //Movement in the Y direction
					if(this.odometer.getY() > 1 && this.odometer.getY() < 30) {
						this.odometer.setY(15 + (firstLeg ? -7 : 7));
					}
					else if( this.odometer.getY() > 30 && this.odometer.getY() < 60 ){
						this.odometer.setY(45 + (firstLeg ? -7 : 7));
					}
					
					LCD.drawString("Correcting Y", 0, 6);
				}
				else if(Math.abs(temp - 90) < 5) { //Movement in the X direction
					if(this.odometer.getX() < 30) {
						this.odometer.setX(15 + (firstLeg ? -7 : 7));
					}
					else {
						this.odometer.setX(45 + (firstLeg ? -7 : 7));
					}
					
					LCD.drawString("Correcting X", 0, 6);

				}
				else { //Not moving explicitly in either direction
					//do nothing
					
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