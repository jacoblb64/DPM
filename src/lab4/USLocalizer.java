package lab4;

import lab3.Driver;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;

public class USLocalizer {
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	public static double ROTATION_SPEED = 30;
	private int FILTER_OUT = 5;

	private Odometer odo;
	private TwoWheeledRobot robot;
	private UltrasonicSensor us;
	private LocalizationType locType;
	private Navigation nav;
	private int filterControl = 0;
	private int filteredDistance = 0;
	
	public USLocalizer(Odometer odo, UltrasonicSensor us, LocalizationType locType) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.us = us;
		this.locType = locType;
		this.nav = new Navigation(odo);
		
		// switch off the ultrasonic sensor
		us.off();
		
	}
	
	public void doLocalization() {
		double [] pos = new double [3];
		double angleA, angleB;
		
		if (locType == LocalizationType.FALLING_EDGE) {
			
			// rotate the robot until it sees no wall
			robot.setRotationSpeed(15);
			while (this.getFilteredData() <= 250);
			Sound.beep();
			
			// keep rotating until the robot sees a wall, then latch the angle
			while (this.getFilteredData() > 40);
			angleA = robot.getHeading();
			Sound.beep();
			
			// switch direction and wait until it sees no wall
			robot.setRotationSpeed(-15);
			while (this.getFilteredData() <= 250);
			Sound.beep();
			
			// keep rotating until the robot sees a wall, then latch the angle
			while (this.getFilteredData() > 40);
			angleB = robot.getHeading();
			Sound.beep();
			
			robot.setSpeeds(0, 0);
			
			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			
			double delTheta = (angleA < angleB ? 45 : 225) - ( (angleA + angleB) / 2);
			
			// update the odometer position (example to follow:)
			odo.setPosition(new double [] {0.0, 0.0, (robot.getHeading() + delTheta + 180) % 360}, new boolean [] {false, false, true});
			
		} else {
			/*
			 * The robot should turn until it sees the wall, then look for the
			 * "rising edges:" the points where it no longer sees the wall.
			 * This is very similar to the FALLING_EDGE routine, but the robot
			 * will face toward the wall for most of it.
			 */
			
			//
			// FILL THIS IN
			//
			
			// turn until it sees the wall
			robot.setRotationSpeed(15);
			while (this.getFilteredData() > 40 && this.getFilteredData() != 0);
			Sound.beep();
			
			// keep going until it sees no wall, and latch the angle
			while (this.getFilteredData() <= 250);
			angleA = robot.getHeading();
			Sound.beep();
			
			// switch directions and wait till it sees a wall
			robot.setRotationSpeed(-15);
			while (this.getFilteredData() > 40);
			Sound.beep();
			
			// keep rotating until it sees no wall, then latch the second angle
			while (this.getFilteredData() <= 250);
			angleB = robot.getHeading();
			Sound.beep();
			
			robot.setSpeeds(0, 0);
			
			double delTheta = (angleA < angleB ? 45 : 225) - ( (angleA + angleB) / 2);
			
			// update the odometer position (example to follow:)
			odo.setPosition(new double [] {0.0, 0.0, (robot.getHeading() + delTheta + 180) % 360}, new boolean [] {false, false, true});
		}
	}
	
	private int getFilteredData() {
		int distance;
		
		// do a ping
		us.ping();
		
		// wait for the ping to complete
		try { Thread.sleep(50); } catch (InterruptedException e) {}
		
		// there will be a delay here
		distance = us.getDistance();
			
		if (distance == 255 && filterControl < FILTER_OUT) {
			// bad value, do not set the distance var, however do increment the filter value
			filterControl ++;
		} else if (distance == 255){
			// true 255, therefore set distance to 255
			filteredDistance = distance;
		} else {
			// distance went below 255, therefore reset everything.
			filterControl = 0;
			filteredDistance = distance;
		}
		
		LCD.drawString("dist: " + filteredDistance, 0, 4);
		
		return filteredDistance;
	}

}
