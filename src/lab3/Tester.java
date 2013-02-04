package lab3;

public class Tester {
	
	public static void main(String[] args) {
		
		WayPoint point1 = new WayPoint(0, 0), point2 = new WayPoint(30, 60);
		
		System.out.println(point1.getForwardError(point2, 0));
		
	}
}
