/*
 * Jacob Barnett : 260451446
 * Haimonti Das : 260445904
 * 
 * Group 4, lab 4
 */

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
		// added to allow for exiting at any time by pressing the bottom button
		
		
		if (Button.waitForAnyPress() == Button.ID_LEFT) {
			LCD.drawString("US FALL", 0, 5);
			// perform the ultrasonic localization
			USLocalizer usl = new USLocalizer(odo, us, USLocalizer.LocalizationType.FALLING_EDGE);
			usl.doLocalization();
		}
		else if (Button.waitForAnyPress() == Button.ID_RIGHT) {
			LCD.drawString("US RISE", 0, 5);
			// perform the ultrasonic localization
			USLocalizer usl = new USLocalizer(odo, us, USLocalizer.LocalizationType.RISING_EDGE);
			usl.doLocalization();
		}
		// triggering which US localization type by button
		
		
		LCD.drawString("LIGHT     ", 0, 5);
		// added printing to show which localizaiton process is running.
		
		// perform the light sensor localization
		LightLocalizer lsl = new LightLocalizer(odo, ls);
		lsl.doLocalization();			
		


		
	}

}
