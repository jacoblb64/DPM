package lab5;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public class Lab5 {

	public static void main(String[] args) {
		
		(new Thread() {
			public void run() {
				while (Button.waitForAnyPress() != Button.ID_ESCAPE);		
				System.exit(0);
			}
		}).start();
	
		
		
		if (Button.waitForAnyPress() == Button.ID_LEFT) {
			
			for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { Motor.A, Motor.C }) {
				motor.setAcceleration(400);
				motor.setSpeed(motor.getMaxSpeed());
			}
			
			try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
			
			for (NXTRegulatedMotor motor2 : new NXTRegulatedMotor[] { Motor.A, Motor.C }) {
				motor2.forward();
			}
		}
		
		if (Button.waitForAnyPress() == Button.ID_RIGHT) {
			
			for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { Motor.A, Motor.C }) {
				motor.setAcceleration(10000);
				motor.setSpeed(motor.getMaxSpeed());
			}
			
			try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
			
			for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { Motor.A, Motor.C }) {
				motor.rotate(30, true);
			}
		}
	
	}
}
