/*
 * add
 */

package lab3;


public class WayPoint {
	private double x;
	private double y;
	
	public WayPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getHeading(WayPoint point2) {
		
		//return 90 -  (Math.atan2(point2.getY() - this.y, point2.getX() - this.x)) / Constants.DEGRAD_CONVERT;
		
		double delY = point2.getY() - this.y, delX = point2.getX() - this.x, angle;
		
		if (delY == 0)	{	//when delta y = 0, an error occurs in calculating the atan
			angle =  delX / Math.abs(delX) * 90.0; //delta x / |delta x| gives 1 when delta x is positive and -1 when delta x is negative 
		} else if (delX == 0 && delY <0)	{ //will return 0 regardless of delta y, only needs correcting when delta y is negative
			angle =  180.0;
		} else {
			angle =  180.0 / Math.PI * Math.atan(delX/delY); //arctangent of (delta x/delta y)
		}

		
		return angle;
	}
	
	public double getAngle(WayPoint point2, double curTheta) {
		
		double delY = point2.getY() - this.y, delX = point2.getX() - this.x, angle;
		
		if (delY == 0)	{	//when delta y = 0, an error occurs in calculating the atan
			angle =  delX / Math.abs(delX) * 90.0; //delta x / |delta x| gives 1 when delta x is positive and -1 when delta x is negative 
		} else if (delX == 0 && delY <0)	{ //will return 0 regardless of delta y, only needs correcting when delta y is negative
			angle =  180.0;
		} else {
			angle =  180.0 / Math.PI * Math.atan(delX/delY); //arctangent of (delta x/delta y)
		}
		
		if(delX > 0 && delY < 0) {
			angle = angle + 180;
		}

		if(delX < 0 && delY < 0) {
			angle = angle - 180;
		}
		
		return getAngularError(angle, curTheta);
	}
	
	public double getAngularError(double destTheta, double curTheta) {
		//double destTheta = getHeading(point2);
		
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
		
		//return (Math.cos(curTheta) * (point2.getX() - this.x)) + (Math.sin(curTheta) * (point2.getY() - this.y));
		
		double v11, v12, v21, v22;
		
		v11 = this.x;
		v12 = this.y;
		v21 = point2.getX();
		v22 = point2.getY();
		
		v21 -= v11;
		v22 -= v12;
		
		double vMag = Math.sqrt((v11 * v11) + (v12 * v12));
		if (vMag != 0) {
			v11 /= vMag;
			v12 /= vMag;
		}
		
		return (v11 * v21) + (v12 * v22);
	}
	
	public double getDistance(WayPoint point2) {
		return Math.sqrt( Math.pow((point2.getX() - this.x), 2) +  Math.pow((point2.getY() - this.y), 2));
	}

	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
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
