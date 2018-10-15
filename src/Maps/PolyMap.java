package Maps;

import Data.Edge;
import Data.Point;
import Data.Polygon;
import java.util.ArrayList;

public class PolyMap {
	
	public ArrayList<Polygon> polygons;
	
	// this class should make the mesh into an identical looking thing where every polygon is it's own object with links to all adjacent polygons
 
	public PolyMap (Point root, ArrayList<Edge> edges) {
		
		createPolygon(root);
		
		for (Edge e : edges) {
			
			e.getLeftSite().getPoly().addEdge(e);
			e.getRightSite().getPoly().addEdge(e);
			e.getLeftSite().getPoly().addAdjacency(e.getRightSite().getPoly());
			e.getRightSite().getPoly().addAdjacency(e.getLeftSite().getPoly());
			
			
			
		}
		
	}
	
	private void createPolygon (Point site) {
		if (site.getLeftChild() != null) {createPolygon(site.getLeftChild());}
		Polygon p = new Polygon (site);
		site.setPoly(p);
		this.polygons.add(p);
		if (site.getRightChild() != null) {createPolygon(site.getRightChild());}
	}
	
}
