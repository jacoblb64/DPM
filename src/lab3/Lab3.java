/*
 * DPM Lab 3
 * Group 4
 * Jacob Barnett : 260451446
 * Haimonti Das :
 */

package lab3;

import lab2.Odometer;
import lab2.OdometryDisplay;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Lab3 {

	public final static Odometer odometer = new Odometer();

	
	public static void main(String[] args) {
		
		int buttonChoice;

		OdometryDisplay odometryDisplay = new OdometryDisplay(odometer);
		//PathDriver pDrive;
		
		do {
			// clear the display
			LCD.clear();

			// TODO: Print choices
			
			LCD.drawString("< Path1| Path2 >", 0, 0);
			LCD.drawString("       |        ", 0, 1);
			LCD.drawString("   No  |Obstacle", 0, 2);
			LCD.drawString("obstacl|    &   ", 0, 3);
			LCD.drawString("4waypts|2 waypts", 0, 4);

			//buttonChoice = Button.waitForPress();
			buttonChoice = Button.waitForAnyPress();
			//*depends on the version
			
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);
		

		odometer.start();
		odometryDisplay.start();
		


		if (buttonChoice == Button.ID_LEFT) {
			// TODO: Implement path1
			(new Thread() {
				public void run() {
					Driver.drive(1, odometer);
				}
			}).start();

		} else {
			// TODO: implement path2
			(new Thread() {
				public void run() {
					Driver.drive(2, odometer);
				}
			}).start();
		}
		
		//while (Button.waitForPress() != Button.ID_ESCAPE);
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		//*depends on the version
		
		System.exit(0);
	}
}
