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
				//System.out.println("0");
				return 0;
			}else if (this.x > P.getX()) {
				//System.out.println("1");
				return 1;
			}else {
				//System.out.println("-1");
				return -1;
			}
		}else if (this.y > P.getY()) {
			//System.out.println("1");
			return 1;
		}else {
			//System.out.println("-1");
			return -1;
		}
		
		
	}
	
	public String toString () {
		return ("x = " + this.x + "\ny = " + this.y);
	}
	
	public double getY () {
		return this.y;
	}public double getX () {
		return this.x;
	}
}
