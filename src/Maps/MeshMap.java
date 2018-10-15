package Maps;

import java.util.ArrayList;
import java.util.PriorityQueue;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import Data.Edge;
import Data.Event;
import Data.Parabola;
import Data.Point;
import Main.Main;
import Main.Window;

public class MeshMap {

	public ArrayList<Point> getSites() {
		return sites;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	ArrayList<Point> sites;
	ArrayList<Edge> edges;
	PriorityQueue<Event> eventQueue; // TODO try and replace this with my own priority queue
	Parabola root;

	int WIDTH;
	int HEIGHT;

	double sweepLineY;  // He's called Ludwig von Sveepline. 

	public MeshMap(int width, int height, ArrayList<Point> sites, Window window, Boolean drawGen, Point rootSite) {

		this.WIDTH = width;
		this.HEIGHT = height;
		this.sites = sites;
		this.edges = new ArrayList<Edge>();

		// this is where fortune's algorithm will run and get me a voronoi
		
		eventQueue = new PriorityQueue<Event>();
		
		/*for (int i = 0; i < sites.size(); i++) {
			eventQueue.add(new Event(sites.get(i), Event.siteEvent)); // fill out the priority queue with events
		}*/

		// start going through the priority queue one at a time

		handleSiteOnTree(rootSite, eventQueue);
		
		while (!eventQueue.isEmpty()) {

			Event e = eventQueue.remove();
			sweepLineY = e.getP().getY();

			if (e.getType()) {
				handleSite(e.getP());
			} else {
				handleIntersection(e);
			}

			/*if (drawGen) {
				drawAll(window);
				try {
					//Thread.sleep(1);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}*/
		}
		
		
		sweepLineY = this.WIDTH + this.HEIGHT; // I have no idea why this bit is happening either, but it didn't work
												// until I copied this in

		endEdges(root);

		for (int i = 0; i < edges.size(); i++) {

			if (edges.get(i).getNeighbour() != null) {
				edges.get(i).setStart(edges.get(i).getNeighbour().getEnd());
				edges.get(i).setNeighbour(null);
			}
		}

		/*for (Edge e: edges) {
			e.describe();
		}*/
		
		
		// I did most of this myself and then stackoverflew the last bits when it didn't
		// work

	}
	
	private void handleSiteOnTree (Point site, PriorityQueue<Event> eventQueue) {
		if (site.getLeftChild() != null) {handleSiteOnTree(site.getLeftChild(), eventQueue);}
		
		eventQueue.add(new Event(site, Event.siteEvent));
		
		if (site.getRightChild() != null) {handleSiteOnTree(site.getRightChild(), eventQueue);}
		
	}

	private void endEdges(Parabola p) {
		
		if (p.getType() == Parabola.focus) {

			p = null;

			return;
		}

		double x = getXofEdge(p);

		p.getEdge().setEnd(new Point(x, p.getEdge().getSlope() * x + p.getEdge().getYint()));
		edges.add(p.getEdge());

		endEdges(p.getLeftChild());
		endEdges(p.getRightChild());

		p = null;

	}

	private void handleSite(Point p) {

		if (root == null) {

			root = new Parabola(p);
			return;
		}

		Parabola para = getParabolaByX(p.getX());
		if (para.getEvent() != null) {

			eventQueue.remove(para.getEvent());
			para.setEvent(null);
		}

		Point start = new Point(p.getX(), getY(para.getPoint(), p.getX()));
		Edge el = new Edge(start, para.getPoint(), p);
		Edge er = new Edge(start, p, para.getPoint());
		el.setNeighbour(er);
		er.setNeighbour(el);
		para.setEdge(el);
		para.setType(Parabola.vertex);

		Parabola p0 = new Parabola(para.getPoint());
		Parabola p1 = new Parabola(p);
		Parabola p2 = new Parabola(para.getPoint());

		para.setLeftChild(p0);
		para.setRightChild(new Parabola());
		para.getRightChild().setEdge(er);
		para.getRightChild().setLeftChild(p1);
		para.getRightChild().setRightChild(p2);

		checkCircleEvent(p0);
		checkCircleEvent(p2);

	}

	// process circle event
	private void handleIntersection(Event e) {

		// find p0, p1, p2 that generate this event from left to right
		Parabola p1 = e.getPara();
		Parabola xl = Parabola.getLeftParent(p1);
		Parabola xr = Parabola.getRightParent(p1);
		Parabola p0 = Parabola.getLeftChild(xl);
		Parabola p2 = Parabola.getRightChild(xr);

		// remove associated events since the points will be altered
		if (p0.getEvent() != null) {
			eventQueue.remove(p0.getEvent());
			p0.setEvent(null);
		}
		if (p2.getEvent() != null) {
			eventQueue.remove(p2.getEvent());
			p2.setEvent(null);
		}

		Point p = new Point(e.getP().getX(), getY(p1.getPoint(), e.getP().getX()));

		// end edges!
		xl.getEdge().setEnd(p);
		xr.getEdge().setEnd(p);
		// System.out.println(xl.get);
		edges.add(xl.getEdge());
		edges.add(xr.getEdge());

		// start new bisector (edge) from this vertex on which ever original edge is
		// higher in tree
		Parabola higher = new Parabola();
		Parabola par = p1;
		while (par != root) {
			par = par.getParent();
			if (par == xl)
				higher = xl;
			if (par == xr)
				higher = xr;
		}
		higher.setEdge(new Edge(p, p0.getPoint(), p2.getPoint()));

		// delete p1 and parent (boundary edge) from beach line
		Parabola gparent = p1.getParent().getParent();
		if (p1.getParent().getLeftChild() == p1) {
			if (gparent.getLeftChild() == p1.getParent())
				gparent.setLeftChild(p1.getParent().getRightChild());
			if (gparent.getRightChild() == p1.getParent())
				gparent.setRightChild(p1.getParent().getRightChild());
		} else {
			if (gparent.getLeftChild() == p1.getParent())
				gparent.setLeftChild(p1.getParent().getLeftChild());
			if (gparent.getRightChild() == p1.getParent())
				gparent.setRightChild(p1.getParent().getLeftChild());
		}

		Point op = p1.getPoint();
		p1.setParent(null);
		p1 = null;

		checkCircleEvent(p0);
		checkCircleEvent(p2);
	}

	// adds circle event if foci a, b, c lie on the same circle
	private void checkCircleEvent(Parabola b) {

		Parabola lp = Parabola.getLeftParent(b);

		Parabola rp = Parabola.getRightParent(b);

		if (lp == null || rp == null) {
			Main.firstIf ++;
			return;
		}
		Parabola a = Parabola.getLeftChild(lp);
		Parabola c = Parabola.getRightChild(rp);

		if (a == null || c == null || a.getPoint().compareTo(c.getPoint()) == 0) {
			Main.secondIf++;
			return;
		}

		if (ccw(a.getPoint(), b.getPoint(), c.getPoint()) != 1) {
			Main.fourthIf++;
			return;
		}

		// edges will intersect to form a vertex for a circle event
		Point start = getEdgeIntersection(lp.getEdge(), rp.getEdge());

		if (start == null) {
			Main.fifthIf ++;
			return;
		}

		// compute radius
		double dx = b.getPoint().getX() - start.getX();
		double dy = b.getPoint().getY() - start.getY();
		double d = Math.sqrt((dx * dx) + (dy * dy));

		if (start.getY() + d < sweepLineY) {
			Main.sixthIf ++;
			return; // must be after sweep line
		}
		Point ep = new Point(start.getX(), start.getY() + d);

		// add circle event

		Event e = new Event(ep, Event.intersectionEvent);
		e.setPara(b);
		b.setEvent(e);
		eventQueue.add(e);
		
		Main.noIfs++;
		
	}

	public int ccw(Point a, Point b, Point c) {

		double area2 = (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX());

		if (area2 < -0.01)
			return -1;
		else if (area2 > 0.01)
			return 1;
		else
			return 0;
	}

	// returns intersection of the lines of with vectors a and b
	private Point getEdgeIntersection(Edge a, Edge b) {

		if (b.getSlope() == a.getSlope() && b.getYint() != a.getYint())
			return null;

		double x = (b.getYint() - a.getYint()) / (a.getSlope() - b.getSlope());
		double y = a.getSlope() * x + a.getYint();

		return new Point(x, y);
	}

	// returns current x-coordinate of an unfinished edge
	private double getXofEdge(Parabola par) {
		// find intersection of two parabolas

		Parabola left = Parabola.getLeftChild(par);
		Parabola right = Parabola.getRightChild(par);

		Point p = left.getPoint();
		Point r = right.getPoint();

		double dp = 2 * (p.getY() - sweepLineY);
		double a1 = 1 / dp;
		double b1 = -2 * p.getX() / dp;
		double c1 = (p.getX() * p.getX() + p.getY() * p.getY() - sweepLineY * sweepLineY) / dp;

		double dp2 = 2 * (r.getY() - sweepLineY);
		double a2 = 1 / dp2;
		double b2 = -2 * r.getX() / dp2;
		double c2 = (r.getX() * r.getX() + r.getY() * r.getY() - sweepLineY * sweepLineY) / dp2;

		double a = a1 - a2;
		double b = b1 - b2;
		double c = c1 - c2;

		double disc = b * b - 4 * a * c;
		double x1 = (-b + Math.sqrt(disc)) / (2 * a);
		double x2 = (-b - Math.sqrt(disc)) / (2 * a);

		double ry;
		if (p.getY() > r.getY())
			ry = Math.max(x1, x2);
		else
			ry = Math.min(x1, x2);

		return ry;
	}

	// returns parabola above this x coordinate in the beach line
	private Parabola getParabolaByX(double xx) {
		Parabola par = root;
		double x = 0;
		while (par.getType() == Parabola.vertex) {
			x = getXofEdge(par);
			if (x > xx)
				par = par.getLeftChild();
			else
				par = par.getRightChild();
		}
		return par;
	}

	// find corresponding y-coordinate to x on parabola with focus p
	private double getY(Point p, double x) {
		// determine equation for parabola around focus p
		double dp = 2 * (p.getY() - sweepLineY);
		double a1 = 1 / dp;
		double b1 = -2 * p.getX() / dp;
		double c1 = (p.getX() * p.getX() + p.getY() * p.getY() - sweepLineY * sweepLineY) / dp;
		return (a1 * x * x + b1 * x + c1);
	}

	private void drawAll(Window window) {

		window.clear();
		Display.update();

		window.beginLineRender();
		window.addVertex(new Vector3f(0, (float) this.sweepLineY, 0));
		window.addVertex(new Vector3f(window.getWIDTH(), (float) this.sweepLineY, 0));
		window.addVertex(new Vector3f(0, (float) this.sweepLineY, 0));
		window.endRender();

		for (Edge e : edges) {
			
			window.beginLineRender();
			if (e.getStart() != null && e.getEnd() != null) {
				window.addVertex(new Vector3f((float)e.getStart().getX(), (float)e.getStart().getY(), 0));
				window.addVertex(new Vector3f((float)e.getEnd().getX(), (float)e.getEnd().getY(), 0));
				window.addVertex(new Vector3f((float)e.getStart().getX(), (float)e.getStart().getY(), 0));
				
			}
			
		}
		
		for (Point site : sites) {
			window.beginLineRender();
			window.addVertex(new Vector3f((float) site.getX() - 1, (float) site.getY(), 0));
			window.addVertex(new Vector3f((float) site.getX() + 1, (float) site.getY(), 0));
			window.addVertex(new Vector3f((float) site.getX() - 1, (float) site.getY(), 0));
			window.endRender();
			window.beginLineRender();
			window.addVertex(new Vector3f((float) site.getX(), (float) site.getY() - 1, 0));
			window.addVertex(new Vector3f((float) site.getX(), (float) site.getY() + 1, 0));
			window.addVertex(new Vector3f((float) site.getX(), (float) site.getY() - 1, 0));
			window.endRender();
		}
		this.root.draw(window, this.sweepLineY);

		Display.update();

	}
}
