package Data;

import org.lwjgl.util.vector.Vector2f;

public class Edge {
	
	Point start;
	Point end;
	Point leftSite;
	Point rightSite;
	Point direction;
	
	Polygon[] polygons;
	
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
		System.out.println("LeftSite = " + this.leftSite);
		System.out.println("RightSite = " + this.rightSite);
		
	}
	
	public Vector2f[] getEq () { // gives a vector equation r = A + (lambda)B where A is eq[0] and B is eq[1]
		Vector2f[] eq = new Vector2f[2];
		 eq[0] = new Vector2f ((float)(this.getStart().getX()), (float)(this.getStart().getY()));
		 eq[1] = new Vector2f ((float)((this.getEnd().getX())-(this.getStart().getX())),(float)((this.getEnd().getY())-(this.getStart().getY())));
		 return eq;
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

	public void addPolygons (Polygon p1, Polygon p2) {
		if (this.polygons == null) {this.polygons = new Polygon[2];}
		if (p1 != null) {this.polygons[0] = p1;}
		if (p2 != null) {this.polygons[1] = p2;}
		}
	
	
	
}
