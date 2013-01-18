package lab1;

import lejos.nxt.*;

public class PController implements UltrasonicController {
	
	private final int bandCenter, bandwith;
	private final int motorStraight = 350, FILTER_OUT = 20 + 7;
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
		
		//for calculations
		double error = this.distance - (bandCenter + 10);
		int differential = (int) Math.abs( 7*error );
		if(differential > 400) differential = 400;
		/* differential will be the amount to change wheel-speeds by
		 * this is a linear function
		 * it caps out at 400 to avoid too extreme speeds
		 */
		
		
		if(Math.abs(error) <= bandwith) { //within bandwith [sic], continue straight
			rightMotor.setSpeed(motorStraight);
			leftMotor.setSpeed(motorStraight);
		}
		else if(error > 0) { //too far, angle in
			differential = (int) (differential*0.4);
			rightMotor.setSpeed(motorStraight + differential);
			leftMotor.setSpeed(motorStraight - differential);
			LCD.drawString("FIRST ELSE IF", 0, 7);
		}
		else { //too close, angle out
			differential = (int) (differential*2.2);
			rightMotor.setSpeed(motorStraight - differential);
			leftMotor.setSpeed(motorStraight + differential);
			LCD.drawString("SECOND ELSE IF", 0, 7);
		}
		
		
		LCD.drawString("Error:" + error, 0, 3);
		LCD.drawString("Diff'l: " + differential, 0, 4);
		LCD.drawString("Rwsp:" + rightMotor.getSpeed(), 0, 5);
		LCD.drawString("Lwsp:" + leftMotor.getSpeed(), 0, 6);
	}

	
	@Override
	public int readUSDistance() {
		return this.distance;
	}

}
