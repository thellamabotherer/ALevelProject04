package Data;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Main.Window;

public class Polygon {
	
	private ArrayList<Edge> edges;
	private Point site;
	private ArrayList<Polygon> adjacencies;
	private boolean inPlate;
	
	private float height;
	
	private Plate plate;
	
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
	
	public void collide (Vector2f direction) {
		
		for (Edge e : edges) {
			// check if eq of edge intersects with bigVect between getStart and getEnd
			if (intersects (
					
					new Vector2f ((float)this.getSite().x, (float)this.getSite().y),
					direction,
					e.getEq()[0],
					e.getEq()[1],
					new Vector2f ((float)this.getSite().x, (float)this.getSite().y),
					new Vector2f ((float)(this.getSite().x + direction.x * 1000),(float)(this.getSite().y + direction.y * 1000)),
					e.getEq()[0],
					new Vector2f ( (float) (e.getEq()[0].x + e.getEq()[1].x) , (float) (e.getEq()[0].y + e.getEq()[1].y) )
					)) {
				
				// this polygon is moving towards the polygon on the other side of edge e, compare the plates and figure out what's happening 
				
				if (e.leftSite.getPoly() == this) {
					this.collideWith (e.rightSite.getPoly());
				}else {
					this.collideWith (e.getLeftSite().getPoly());
				}
				
			}
		}
		
	}public void collideWith (Polygon P) {
		
		Plate plate1 = this.getPlate();
		Plate plate2 = P.getPlate();
		
		Random rand = new Random();
		
		double dist = Math.sqrt((this.getSite().x - P.getSite().x)*(this.getSite().x - P.getSite().x) + 
							   (this.getSite().y - P.getSite().y)*(this.getSite().y - P.getSite().y));
		double afterDist = Math.sqrt((this.getSite().x - this.getPlate().getDirection().x * 0.0001 - P.getSite().x + P.getPlate().getDirection().x)*(this.getSite().x - this.getPlate().getDirection().x * 0.0001 - P.getSite().x + P.getPlate().getDirection().x) + 
				(this.getSite().y - this.getPlate().getDirection().y * 0.0001 - P.getSite().y + P.getPlate().getDirection().y)*(this.getSite().y - this.getPlate().getDirection().y * 0.0001 - P.getSite().y + P.getPlate().getDirection().y));

		if ((dist - afterDist) > 0) { // moving towards
			
			if ((plate1.isContinental() && plate2.isContinental()) || (!plate1.isContinental() && !plate2.isContinental())) {

				// produce tall mountains (quite steep and regular ridgeline)
				// low volcanic activity
				
				// set a height with semi-random parameters
				
				this.height = (float) (this.height + 1.5 + rand.nextFloat());
				
				// set ridge irregularity value 
				
				float ridgeIrregularity = (float)0.5;
				
				// adjust height if necessary for ridge irregularity
				
				for (Polygon p : this.adjacencies) {
					
					if (Math.abs(this.height - p.getHeight()) > ridgeIrregularity) {
						
						if (this.height - p.getHeight() > 0) {
							this.height = p.getHeight() + ridgeIrregularity;
						}else {
							this.height = p.getHeight() - ridgeIrregularity;
						}
						
					}
					
				}
				
				// set a steepness value 
				// recurse into adjacent polygons to adjust height if height enough to cause this 
				
			}else {
				
				// produce medium height mountains (not very steep and quite regular ridge) on continental plate with dropoff into a very steep trench on the oceanic 
				// medium volcanic activity
				
			}
			
		}else { // moving away 
			// produce medium height mountains (average steepness and irregular ridgeline)
			// high volcanic activity
		}
		
	}
	
	private static boolean intersects (Vector2f Vec1A, Vector2f Vec1B, Vector2f Vec2A, Vector2f Vec2B, Vector2f Vec1Start, Vector2f Vec1End, Vector2f Vec2Start, Vector2f Vec2End) {
		
		float lambda;
		float meiou;
		
		try {
			meiou = ((Vec1B.y*(Vec2A.x - Vec1A.x) - Vec1B.x*(Vec2A.y - Vec1A.y))/(Vec1B.x*Vec2B.y - Vec1B.y*Vec2B.x));
			lambda = (((Vec2A.x - Vec1A.x) + Vec2B.x * meiou) / Vec1B.x);
		} catch (ArithmeticException ex) { // div 0 error implies parallel 
			return false;
		}
		float Vec1X = Vec1A.x + Vec1B.x * lambda;
		float Vec1Y = Vec1A.y + Vec1B.y * lambda;
		
		if ((Vec1Start.x <= Vec1X) &&
				(Vec1End.x >= Vec1X) &&
				(Vec2Start.x <= Vec1X) &&
				(Vec2End.x >= Vec1X) &&
				(Vec1Start.y <= Vec1Y) &&
				(Vec1End.y >= Vec1Y) &&
				(Vec2Start.y <= Vec1Y) &&
				(Vec2End.y >= Vec1Y)) {
			return true;
		}return false;
		
		
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
	
	public float getHeight () {
		return this.height;
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

	public Plate getPlate() {
		return plate;
	}

	public void setPlate(Plate plate) {
		this.plate = plate;
		if (this.plate.isContinental()) {
			this.height = 1;
		}else {
			this.height = -1;
		}
	}
	
}
