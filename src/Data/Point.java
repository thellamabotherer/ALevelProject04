package Data;

import java.util.Random;

public class Point implements Comparable<Point> {

	double x;
	double y;
	boolean left; // this is just for the moving points
	boolean up;
	Point parent;
	Point leftChild;
	Point rightChild;

	private Polygon poly;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
		Random rand = new Random ();
		this.left = rand.nextBoolean();
		this.up = rand.nextBoolean();
	}public Point(double x, double y, boolean left, boolean up) {
		this.x = x;
		this.y = y;
		//Random rand = new Random ();
		this.left = left;
		this.up = up;
	}

	public int compareTo(Point P) {

		if (this.y == P.getY()) {
			if (this.x == P.getX()) {
				return 0;
			} else if (this.x > P.getX()) {
				return 1;
			} else {
				return -1;
			}
		} else if (this.y > P.getY()) {
			return 1;
		} else {
			return -1;
		}

	}

	public Polygon getPoly() {
		return poly;
	}

	public void setPoly(Polygon poly) {
		this.poly = poly;
	}

	public Point getParent() {
		return parent;
	}

	public void setParent(Point parent) {
		this.parent = parent;
	}

	public Point getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(Point leftChild) {
		this.leftChild = leftChild;
	}

	public Point getRightChild() {
		return rightChild;
	}

	public void setRightChild(Point rightChild) {
		this.rightChild = rightChild;
	}

	public String toString() {
		return ("x = " + this.x + "\ny = " + this.y);
	}

	public double getY() {
		return this.y;
	}

	public double getX() {
		return this.x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void addSmall() {
		this.y = this.y + 0.000000001;
	}

	public void slide(float dist, int WIDTH, int HEIGHT) { // this method is also useless, avoid it at all costs

		if (this.left) {
			this.x = this.x - dist;
			if (this.x < 0) {
				this.x = this.x + dist * 2;
				this.left = false;
			}
		} else {
			this.x = this.x + dist;
			if (this.x >= WIDTH) {
				this.x = this.x - dist * 2;
				this.left = true;
			}
		}

		if (!this.up) {
			this.y = this.y - dist;
			if (this.y < 0) {
				this.y = this.y + dist * 2;
				this.up = true;
			}
		} else {
			this.y = this.y + dist;
			if (this.y >= HEIGHT) {
				this.y = this.y - dist * 2;
				this.up = false;
			}
		}
	}
	
	public boolean getUp () {
		return this.up;
	}public boolean getLeft () {
		return this.left;
	}

}
