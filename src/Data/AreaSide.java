package Data;

import java.util.ArrayList;

public class AreaSide {

	Point p1;
	float h1;
	
	Point p2;
	float h2;
	
	Area a1;
	Area a2;

	private ArrayList<AreaSide> adj1;
	private ArrayList<AreaSide> adj2;

	public AreaSide(Point p1, Point p2, Area a1, Area a2) {
		this.p1 = p1;
		this.p2 = p2;
		this.a1 = a1;
		this.a2 = a2;
	}

	public AreaSide(Edge e) {
		this.p1 = e.getStart();
		this.p2 = e.getEnd();
		this.a1 = e.polygons[0].getArea();
		this.a2 = e.polygons[1].getArea();
	}

	public void setupAdj() {
		Point buffer1 = p1;
		Point buffer2 = p2;
		if (adj1 != null) {
			adj1 = new ArrayList();
			for (Area a : a1.getAdjacencies()) {
				for (Area b : a.getAdjacencies()) {
					if (b == a2) {
						for (AreaSide s : b.getSides()) {
							if (s.getA1() == a1 || s.getA1() == a2 || s.getA2() == a1 || s.getA2() == a2) {
								adj1.add(s);
							}
						}
					}
				}
			}
		}
		if (adj2 != null) {
			adj2 = new ArrayList();
			for (Area a : a2.getAdjacencies()) {
				for (Area b : a.getAdjacencies()) {
					if (b == a1) {
						for (AreaSide s : b.getSides()) {
							if (s.getA1() == a1 || s.getA1() == a2 || s.getA2() == a1 || s.getA2() == a2) {
								adj1.add(s);
							}
						}
					}
				}
			}
		}
	}

	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	public Area getA1() {
		return a1;
	}

	public void setA1(Area a1) {
		this.a1 = a1;
	}

	public Area getA2() {
		return a2;
	}

	public void setA2(Area a2) {
		this.a2 = a2;
	}

}
