package lab5;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import bluetooth.BluetoothConnection;

public class Lab5 {

	public static void main(String[] args) {
		
		(new Thread() {
			public void run() {
				while (Button.waitForAnyPress() != Button.ID_ESCAPE);		
				System.exit(0);
			}
		}).start();
		
		try {
			Exception la = new Exception();
			throw la;
			/*
			BluetoothConnection btooth = new BluetoothConnection();
			btooth.getTransmission();
			btooth.printTransmission();
			
			catapult();
			*/
		}
		catch (Exception ee){		
			if (Button.waitForAnyPress() == Button.ID_LEFT) {
				flywheel();
			}
			
			if (Button.waitForAnyPress() == Button.ID_RIGHT) {			
				catapult();
			}
		}
	}
	
	private static void flywheel() {
		for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { Motor.A, Motor.B, Motor.C   }) {
			motor.setAcceleration(400);
			motor.setSpeed(motor.getMaxSpeed());
		}
		
		try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
		
		for (NXTRegulatedMotor motor2 : new NXTRegulatedMotor[]  {Motor.A, Motor.B, Motor.C }) {
			motor2.forward();
		}
	}
	
	private static void catapult() {
		for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { Motor.A, Motor.B, Motor.C }) {
			motor.setAcceleration(10000);
			motor.setSpeed(motor.getMaxSpeed());
		}
		
		try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
		
		for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { Motor.A, Motor.B, Motor.C }) {
			motor.rotate(110 , true);
		}
	}
}
