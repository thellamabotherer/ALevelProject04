package Data;

public class Edge {
	
	Point start;
	Point end;
	Point leftSite;
	Point rightSite;
	Point direction;
	
	private Edge neighbour;
	
	double slope;
	double yint;
	
	public Edge (Point first, Point left, Point right) {
		start = first;
		leftSite = left;
		rightSite = right;
		direction = new Point (right.y - right.y, - (right.x - left.x));
		end = null;
		slope = (right.x - left.x)/(left.y - right.y);
		Point mid = new Point ((right.x + left.x)/2, (right.y + left.y)/2);
		yint = mid.y - slope*mid.x;
	}
	
	public void describe () {
		System.out.println("Start = " + this.start);
		System.out.println("End = " + this.end);
		
	}
	
	public Edge getNeighbour () {
		return this.neighbour;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public void setNeighbour(Edge neighbour) {
		this.neighbour = neighbour;
	}

	public Point getLeftSite() {
		return leftSite;
	}

	public void setLeftSite(Point leftSite) {
		this.leftSite = leftSite;
	}

	public Point getRightSite() {
		return rightSite;
	}

	public void setRightSite(Point rightSite) {
		this.rightSite = rightSite;
	}

	public Point getDirection() {
		return direction;
	}

	public void setDirection(Point direction) {
		this.direction = direction;
	}

	public double getSlope() {
		return slope;
	}

	public void setSlope(double slope) {
		this.slope = slope;
	}

	public double getYint() {
		return yint;
	}

	public void setYint(double yint) {
		this.yint = yint;
	}

}
