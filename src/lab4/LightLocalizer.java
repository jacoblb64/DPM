package lab4;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Sound;

public class LightLocalizer {
	private Odometer odo;
	private TwoWheeledRobot robot;
	private LightSensor ls;
	private Navigation nav;
	private static final long CORRECTION_PERIOD = 60;
	
	
	public LightLocalizer(Odometer odo, LightSensor ls) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.ls = ls;
		this.nav = this.odo.getNavigation();
		
		// turn on the light
		ls.setFloodlight(true);
	}
	
	public void doLocalization() {
		
		// drive to location listed in tutorial
		//nav.travelToStraight(7.5, 7.5);
		nav.turnTo(0, false);
		
		// start rotating and clock all 4 gridlines
		robot.setSpeeds(0, -15);
		
		
		// declaring variables for the rotation process
		long correctionStart, correctionEnd;
		int lastLight = 0;
		double cur = 0.0;
		boolean isLine = false;
		double[] angles = new double[4];
		int i = 0;
		
		while (i < 4) {
			correctionStart = System.currentTimeMillis();
			LCD.drawString("i:" + i, 0, 4);
			
			if(lastLight == 0) {
				lastLight = ls.getNormalizedLightValue();
				continue;
			} // initialization, set lastLight then move to next iteration
			else cur = ls.getNormalizedLightValue() - lastLight;
			// usual case, calculate the difference
			
			isLine = (cur < -45);
			lastLight = ls.getNormalizedLightValue();
			// if the difference is large, there's a line, then set the lastLight
			// only the negative is used to only trigger detection on one side of the line, not both
			
			LCD.drawString("iL:" + isLine, 0, 6);
			LCD.drawString("LV:" + ls.getNormalizedLightValue(), 4, 4);
			LCD.drawString("c:" + cur, 10, 4);
			// printing information
			
			if (isLine && ( i == 0 || Math.abs(odo.getAng() - angles[i-1]) > 30)) {
				// only detect one line per 30 degrees, to avoid double detection and false positives
				
				Sound.beep();
				angles[i] = odo.getAng();
				i++;
				isLine = false;
				LCD.drawString("" + angles[i-1], 10, 6);
				continue;
				// add the value to the array and increment, then move to the next iteration
			}
			
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
		
		//robot.setSpeeds(0, 0);
		
		
		
		// do trig to compute (0,0) and 0 degrees
		double thetaY2 = (angles[0] - angles[2]) / 2.0;
		double thetaX2 = (angles[1] - angles[3]) / 2.0;
		
		odo.setPosition(new double [] {-Math.abs(Constants.SENSOR_DISTANCE*Math.cos(thetaY2) / 10), -Math.abs(Constants.SENSOR_DISTANCE*Math.cos(thetaX2) / 10), 0}, new boolean [] {true, true, false});

		
		// when done travel to (0,0) and turn to 0 degrees
		/*
		//nav.travelToStraight(0, 0);
		nav.turnToSimple(0);
		nav.travelStraight(Math.abs(odo.getY()));
		nav.turnToSimple(90);
		nav.travelStraight(Math.abs(odo.getX()));
		nav.turnToSimple(0);
		*/
		
		nav.travelToSimple(0,2);
		nav.turnToSimple(5);
		// values are offset to correct the robot by the same amount every time. From observed trial and error.
		
		odo.setPosition(new double [] {0, 0, 0}, new boolean [] {true, true, true});
		// this position and orientation is 0, 0, 0
		

	}
}
