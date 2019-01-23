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

	private River R;
	private float water ;
	private AreaSide next;
	private boolean r = false;
	private boolean tried = false;
	
	public AreaSide(Area a1, Area a2) {
		for (Edge e : a1.getPoly().getEdges()) {
			if (e.leftSite == a1.getPoly().getSite() && e.rightSite == a2.getPoly().getSite()
					|| e.leftSite == a2.getPoly().getSite() && e.rightSite == a1.getPoly().getSite()) {
				this.p1 = e.start;
				this.p2 = e.end;
				this.a1 = a1;
				this.a2 = a2;
			}
		}
	}

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
		for (Area a : a1.getAdjacencies()) {
			for (Area b : a2.getAdjacencies()) {
				if (a == b) {
					if (adj1 == null) {
						adj1 = new ArrayList();
						adj1.add(a.sideBetween(a1));
						adj1.add(a.sideBetween(a2));
					}else {
						adj2 = new ArrayList();
						adj2.add(a.sideBetween(a1));
						adj2.add(a.sideBetween(a2));
					}
				}
			}
		}
	}
	
	public ArrayList<Area> ends () {
		ArrayList<Area> l = new ArrayList();
		System.out.println(this);
		for (Area a : this.a1.getAdjacencies()) {
			for (Area b : this.a2.getAdjacencies()) {
				if (a == b) {
					l.add(a);
				}
			}
		}return l;
	}
	
	public ArrayList<AreaSide> getAdjOnArea (Area a) {
		//System.out.println("Adj on area");
		ArrayList<AreaSide> adj = new ArrayList();
		for (AreaSide s : adj1) {
			if (s.a1 == a || s.a2 == a) {
				adj.add(s);
			}
		}for (AreaSide s : adj2) {
			if (s.a1 == a || s.a2 == a) {
				adj.add(s);
			}
		}
		
		//System.out.println("adj size = " + adj.size());
		return adj;
	}
	public ArrayList<AreaSide> getAdj () {
		//System.out.println("Adj on area");
		ArrayList<AreaSide> adj = new ArrayList();
		for (AreaSide s : adj1) {
			adj.add(s);	
		}for (AreaSide s : adj2) {
			adj.add(s);
		}
		//System.out.println("adj size = " + adj.size());
		return adj;
	}
	
	
	
	public boolean hasRiver () {
		return r;
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

	public River getR() {
		return R;
	}

	public void setR(River r) {
		R = r;
		this.r = true;
		tried = true;
	}
	
	public void deleteR() {
		R = null;
		r = false;
	}

	public float getWater() {
		return water;
	}

	public void setWater(float water) {
		this.water = water;
	}

	public AreaSide getNext() {
		return next;
	}

	public void setNext(AreaSide next) {
		this.next = next;
	}

	public boolean isTried() {
		return tried;
	}

	public void setTried(boolean tried) {
		this.tried = tried;
	}

	public ArrayList<AreaSide> getAdj1() {
		return adj1;
	}

	public void setAdj1(ArrayList<AreaSide> adj1) {
		this.adj1 = adj1;
	}

	public ArrayList<AreaSide> getAdj2() {
		return adj2;
	}

	public void setAdj2(ArrayList<AreaSide> adj2) {
		this.adj2 = adj2;
	}
	
	public Area getEnd () {
		for (Area a : this.a1.getAdjacencies()) {
			for (Area b : this.a2.getAdjacencies()) {
				if (a==b) {
					return a;
				}
			}
		}System.out.println("Returned null (get end)");
		return null;
	}
	
}
