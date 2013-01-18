package lab1;

import lejos.nxt.*;

public class PController implements UltrasonicController {
	
	private final int bandCenter, bandwith;
	private final int motorStraight = 200, FILTER_OUT = 20;
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;	
	private int distance;
	private int currentLeftSpeed;
	private int filterControl;
	
	public PController(int bandCenter, int bandwith) {
		//Default Constructor
		this.bandCenter = bandCenter;
		this.bandwith = bandwith;
		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);
		leftMotor.forward();
		rightMotor.forward();
		currentLeftSpeed = 0;
		filterControl = 0;
	}
	
	@Override
	public void processUSData(int distance) {
		
		// rudimentary filter
		if (distance == 255 && filterControl < FILTER_OUT) {
			// bad value, do not set the distance var, however do increment the filter value
			filterControl ++;
		} else if (distance == 255){
			// true 255, therefore set distance to 255
			this.distance = distance;
		} else {
			// distance went below 255, therefore reset everything.
			filterControl = 0;
			this.distance = distance;
		}
		// TODO: process a movement based on the us distance passed in (P style)
		
		double error = this.distance - bandCenter;
		int differential = (int) Math.abs( 5*error );
		differential %= 500;
		
		LCD.drawString("Error:" + error, 0, 3);
		LCD.drawString("Differential: " + differential, 0, 4);
		LCD.drawString("Rwsp:" + rightMotor.getSpeed(), 0, 5);
		LCD.drawString("Lwsp:" + leftMotor.getSpeed(), 0, 6);
		
		if(Math.abs(error) <= bandwith) {
			rightMotor.setSpeed(motorStraight);
			leftMotor.setSpeed(motorStraight);
		}
		else if(error > 0) { //too far
			rightMotor.setSpeed(motorStraight + differential);
			leftMotor.setSpeed(motorStraight - 2*differential);
		}
		else { //too close
			rightMotor.setSpeed(motorStraight - 2*differential);
			leftMotor.setSpeed(motorStraight + differential);
		}
		
	}

	
	@Override
	public int readUSDistance() {
		return this.distance;
	}

}
