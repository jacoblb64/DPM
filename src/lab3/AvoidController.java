package lab3;

import lab1.UltrasonicController;

public class AvoidController implements UltrasonicController {

	private int distance;

	
	public AvoidController() {
		// nothing needs to be done
	}
	
	@Override
	public void processUSData(int distance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int readUSDistance() {
		return this.distance;
	}

}
