package Data;

public class Point implements Comparable <Point>{
	
	double x;
	double y;
	
	public Point (double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public int compareTo (Point P) {
		if (this.y == P.getY()) {
			if (this.x == P.getX()) {
				return 0;
			}else if (this.x > P.getX()) {
				return 1;
			}else {
				return -1;
			}
		}else if (this.y > P.getY()) {
			return 1;
		}else {
			return -1;
		}
	}
	
	public double getY () {
		return this.y;
	}public double getX () {
		return this.x;
	}
}
