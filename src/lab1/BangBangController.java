package lab1;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class BangBangController implements UltrasonicController{
	private final int bandCenter, bandwith;
	private final int motorLow, motorHigh;
	private final int motorStraight = 200;
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;
	private int distance;
	private int currentLeftSpeed;
	private int dCount;
	
	public BangBangController(int bandCenter, int bandwith, int motorLow, int motorHigh) {
		//Default Constructor
		this.bandCenter = bandCenter + 10;
		this.bandwith = bandwith;
		this.motorLow = motorLow;
		this.motorHigh = motorHigh;
		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);
		leftMotor.forward();
		rightMotor.forward();
		currentLeftSpeed = 0;
	}
	
	@Override
	public void processUSData(int distance) {
		this.distance = distance;
		// TODO: process a movement based on the us distance passed in (BANG-BANG style)
		
		
		
		double error = this.distance - bandCenter;
		
		if(distance >= 250 && dCount < 25) {
			rightMotor.setSpeed(motorStraight);
			leftMotor.setSpeed(motorStraight);
			
			dCount++;
			return;
		}
		
		
		if(distance < 250) {
			dCount = 0;
		}
		
		if(Math.abs(error) <= bandwith) {
			
			rightMotor.setSpeed(motorStraight);
			leftMotor.setSpeed(motorStraight);
		}

		else if(error > 0) { //too far
			rightMotor.setSpeed(motorHigh);
			leftMotor.setSpeed(motorLow - 50);
		}
		else { //too close
			rightMotor.setSpeed(motorLow - 50);
			leftMotor.setSpeed(motorHigh);
		}
		
	}

	@Override
	public int readUSDistance() {
		return this.distance;
	}
}
