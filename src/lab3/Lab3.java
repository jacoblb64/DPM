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

	public static void main(String[] args) {
		
		int buttonChoice;

		Odometer odometer = new Odometer();
		OdometryDisplay odometryDisplay = new OdometryDisplay(odometer);
		
		do {
			// clear the display
			LCD.clear();

			// TODO: Print choices
			
			LCD.drawString("< Path1 | Path2 >", 0, 0);
			LCD.drawString("        |        ", 0, 1);
			LCD.drawString("   No   |Obstacle", 0, 2);
			LCD.drawString("obstacle|    &   ", 0, 3);
			LCD.drawString("4 waypts|2 waypts ", 0, 4);

			//buttonChoice = Button.waitForPress();
			buttonChoice = Button.waitForAnyPress();
			//*depends on the version
			
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);
		
		odometer.start();
		odometryDisplay.start();

		if (buttonChoice == Button.ID_LEFT) {
			// TODO: Implement path1


		} else {
			// TODO: implement path2

		}
		
		
		//while (Button.waitForPress() != Button.ID_ESCAPE);
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		//*depends on the version
		
		System.exit(0);
	}
}
