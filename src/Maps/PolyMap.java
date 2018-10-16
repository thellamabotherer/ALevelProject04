package Maps;

import Data.Edge;
import Data.Point;
import Data.Polygon;
import java.util.ArrayList;
import java.util.Random;

public class PolyMap {
	
	private ArrayList<Polygon> polygons;
	private int count = 0;
	
	// this class should make the mesh into an identical looking thing where every polygon is it's own object with links to all adjacent polygons
 
	public PolyMap (Point root, ArrayList<Edge> edges, ArrayList<Point> sites) {
		
		this.polygons = new ArrayList();
		
		//createPolygon(root);
		
		for (Point site : sites) {
			
			Polygon p = new Polygon (site);
			site.setPoly(p);
			this.polygons.add(p);
			
		}

		for (Edge e : edges) {
			
			if (e.getLeftSite() != null) {e.getLeftSite().getPoly().addEdge(e);
										  e.getLeftSite().getPoly().addAdjacency(e.getRightSite().getPoly());
										  e.addPolygons(e.getLeftSite().getPoly(), null);}
			if (e.getRightSite() != null) {e.getRightSite().getPoly().addEdge(e);
			                               e.getRightSite().getPoly().addAdjacency(e.getLeftSite().getPoly());
			                               e.addPolygons(null, e.getLeftSite().getPoly());}
			
		}
		
		
		
	}
	
	public ArrayList<Point> relax (float dist) {
		ArrayList<Point> newSites = new ArrayList();
		for (Polygon p : this.polygons) {
			p.relaxPoly(dist);
			newSites.add(p.getSite());
		}return newSites;
	}
	
	private void createPolygon (Point site) {
		count ++;
		if (site.getLeftChild() != null) {createPolygon(site.getLeftChild());}
		Polygon p = new Polygon (site);
		site.setPoly(p);
		this.polygons.add(p);
		if (site.getRightChild() != null) {createPolygon(site.getRightChild());}
	}
	
	public ArrayList<Polygon> getPolys () {
		return this.polygons;
	}
	
}
