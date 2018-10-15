package Data;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Main.Window;

public class Polygon {
	
	private ArrayList<Edge> edges;
	private Point site;
	private ArrayList<Polygon> adjacencies;
	private boolean inPlate;
	
	public Polygon(Point site) {
		this.site = site;
		this.edges = new ArrayList();
		this.adjacencies = new ArrayList();
		this.inPlate = false;
	}

	public void relaxPoly (float dist) { // a dist of 1 would move the point to the centroid, a dist of 0.5 would move it halfway etc
		Vector3f centroid = this.getCentroid();
		float xDist = centroid.x - (float)this.site.x;
		float yDist = centroid.y - (float)this.site.y;
		this.site.setX(this.site.getX() + (dist * xDist));
		this.site.setY(this.site.getY() + (dist * yDist));
	}private Vector3f getCentroid () {
		double xSum = 0;
		int xN = 0;
		double ySum = 0;
		int yN = 0;
		for (Edge e: edges) {
			xSum = (xSum + e.getStart().x);
			xN ++;
			ySum = (ySum + e.getStart().y); 
			yN++;
		}return (new Vector3f ((float)xSum/xN, (float)ySum/yN, (float)0));
	}
	
	public void draw (Window window, Vector4f colour) {
		
		window.changeColour(colour);
		
		for (Edge e : this.edges) {
			window.beginRender();
			window.addVertex(new Vector3f((float)this.site.x, (float)this.site.y, 0));
			window.addVertex(new Vector3f((float)e.getStart().x, (float)e.getStart().y, 0));
			window.addVertex(new Vector3f((float)e.getEnd().x, (float)e.getEnd().y, 0));
			window.endRender();
		}
		
	}
	
	public void addEdge (Edge e) {
		this.edges.add(e);
	}public void addAdjacency (Polygon p) {
		this.adjacencies.add(p);
	}public boolean isInPlate () {
		return this.inPlate;
	}public void setInPlate (boolean b) {
		this.inPlate = b;
	}

	public ArrayList<Polygon> getAdjacencies() {
		return adjacencies;
	}

	public void setAdjacencies(ArrayList<Polygon> adjacencies) {
		this.adjacencies = adjacencies;
	}

	public Point getSite() {
		return site;
	}

	public void setSite(Point site) {
		this.site = site;
	}
	
}
