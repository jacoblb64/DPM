package lab4;

import lab3.Driver;
import lejos.nxt.*;

public class Lab4 {

	public static void main(String[] args) {
		// setup the odometer, display, and ultrasonic and light sensors
		TwoWheeledRobot patBot = new TwoWheeledRobot(Motor.A, Motor.C);
		Odometer odo = new Odometer(patBot, true);
		LCDInfo lcd = new LCDInfo(odo);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
		LightSensor ls = new LightSensor(SensorPort.S2);
		
		//Button.waitForAnyPress();
		
		(new Thread() {
			public void run() {
				while (Button.waitForAnyPress() != Button.ID_ESCAPE);		
				System.exit(0);
			}
		}).start();
		
		if (Button.waitForAnyPress() == Button.ID_LEFT) {
			// perform the ultrasonic localization
			USLocalizer usl = new USLocalizer(odo, us, USLocalizer.LocalizationType.FALLING_EDGE);
			usl.doLocalization();
		}
		else if (Button.waitForAnyPress() == Button.ID_RIGHT) {
			// perform the ultrasonic localization
			USLocalizer usl = new USLocalizer(odo, us, USLocalizer.LocalizationType.RISING_EDGE);
			usl.doLocalization();
		}
		
		// perform the light sensor localization
		LightLocalizer lsl = new LightLocalizer(odo, ls);
		lsl.doLocalization();			
		


		
	}

}
