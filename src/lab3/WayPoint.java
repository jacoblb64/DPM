package lab3;

public class WayPoint {
	private double x;
	private double y;
	
	public WayPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getHeading(WayPoint point2) {
		
		return Math.atan2(point2.getY() - this.y, point2.getX() - this.x);
	}
	
	public double getAngularError(WayPoint point2, double curTheta) {
		double destTheta = getHeading(point2);
		
		double error = destTheta - curTheta;
		
		if (error >= -180 && error <= 180) {
			return error;
		}
		else if (error < -180) {
			return error + 360;
		}
		else {
			return error - 360;
		}
	}
	
	public double getForwardError(WayPoint point2, double curTheta) {
		
		return (Math.cos(curTheta) * (point2.getX() - this.x)) + (Math.sin(curTheta) * (point2.getY() - this.y));
	}

	
	// getters and setters
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	
}
