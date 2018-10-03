package Data;

public class Event implements Comparable <Event> {
	
	public static boolean siteEvent = true;
	public static boolean intersectionEvent = false;
	
	Point P;
	boolean type;
	Parabola para;
	
	public Event (Point p, boolean type) {
		this.P = p;
		this.type = type;
		this.para = null;
	}
	
	public int compareTo (Event e) {
		return this.P.compareTo(e.getP());
	}
	
	public Point getP () {
		return this.P;
	}public boolean getType () {
		return this.type;
	}

	public Parabola getPara() {
		return para;
	}

	public void setPara(Parabola para) {
		this.para = para;
	}

	public void setP(Point p) {
		P = p;
	}

	public void setType(boolean type) {
		this.type = type;
	}

}
