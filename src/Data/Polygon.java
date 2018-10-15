package Data;

import java.util.ArrayList;

public class Polygon {
	
	private ArrayList<Edge> edges;
	private Point site;
	private ArrayList<Polygon> adjacencies;
	
	public Polygon(Point site) {
		this.site = site;
		this.edges = new ArrayList();
		this.adjacencies = new ArrayList();
	}

	public void addEdge (Edge e) {
		this.edges.add(e);
	}public void addAdjacency (Polygon p) {
		this.adjacencies.add(p);
	}
	
}
