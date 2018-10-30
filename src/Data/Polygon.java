package Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Main.Window;
import Main.WorldConstraints;

public class Polygon {

	private ArrayList<Edge> edges;
	private Point site;
	private ArrayList<Polygon> adjacencies;
	private boolean inPlate;
	private boolean checked = false;

	private float height;
	private boolean edgeOfPlate = false;
	private int boundaryType; //

	private Plate plate;

	public Polygon(Point site) {
		this.site = site;
		this.edges = new ArrayList();
		this.adjacencies = new ArrayList();
		this.inPlate = false;
	}

	public void relaxPoly(float dist) { // a dist of 1 would move the point to the centroid, a dist of 0.5 would move it
										// halfway etc
		Vector3f centroid = this.getCentroid();
		float xDist = centroid.x - (float) this.site.x;
		float yDist = centroid.y - (float) this.site.y;
		this.site.setX(this.site.getX() + (dist * xDist));
		this.site.setY(this.site.getY() + (dist * yDist));
	}

	public Vector3f getCentroid() {
		double xSum = 0;
		int xN = 0;
		double ySum = 0;
		int yN = 0;
		for (Edge e : edges) {
			xSum = (xSum + e.getStart().x);
			xN++;
			ySum = (ySum + e.getStart().y);
			yN++;
		}
		return (new Vector3f((float) xSum / xN, (float) ySum / yN, (float) 0));
	}

	public void draw(Window window, Vector4f colour) {

		window.changeColour(colour);

		for (Edge e : this.edges) {
			window.beginRender();
			window.addVertex(new Vector3f((float) this.site.x, (float) this.site.y, 0));
			window.addVertex(new Vector3f((float) e.getStart().x, (float) e.getStart().y, 0));
			window.addVertex(new Vector3f((float) e.getEnd().x, (float) e.getEnd().y, 0));
			window.endRender();
		}

	}

	public float distToPoly(Polygon P) {
		float dx = this.getCentroid().x - P.getCentroid().x;
		float dy = this.getCentroid().y - P.getCentroid().y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	public void checkEdge() {
		for (Edge e : this.edges) {
			if (e.getRightSite().getPoly().getPlate() != this.getPlate()
					|| e.getLeftSite().getPoly().getPlate() != this.getPlate()) {
				this.edgeOfPlate = true;
				return;
			}
		}
		this.edgeOfPlate = false;
	}

	public boolean isEdgeOfPlate() {
		return edgeOfPlate;
	}



	public int distToPlate(Plate P, int steps, int best, Plate current) {
		
		if (this.getPlate() == P) {
			return 0;
		}
		if (steps > best || this.plate != current) {
			return best;
		}
		int localBest = 50;
		boolean found = false;
		int newDist;
		for (Polygon p : this.getAdjacencies()) {
			if (!p.isChecked()) {
				newDist = (p.distToPlate(P, steps + 1, localBest, current));
				if (newDist != -1) {
					found = true;
					if (newDist < localBest) {
						localBest = newDist;
					}
				}
			}
		}this.checked = true;
		if (found) {
			return localBest + 1;
		}
		return -1;
	}

	public void calculateElevation() {

		ArrayList<Float[]> tectonicEffects = new ArrayList();
		Random rand = new Random();
		
		// for each plate around the plate this is on

		float heightDiff = 0;

		//System.out.println("On plate : " + this.plate);
		
		for (Plate P : this.getPlate().getAdj()) {

			//System.out.println("Searching for plate : " + P);
			
			// get dist to nearest point on that plate

			int dist = distToPlate(
					P, 
					0, 
					50, 
					this.plate
					);
			this.plate.uncheck();
			float relSpeed = relSpeed(this.getPlate().getDirection(), P.getDirection());

			// calc elevation based on params

			if (movingCloser(this.plate, P)) { // plates moving together
				
				if (this.getPlate().isContinental() && P.isContinental()) {
					// mountains up
					heightDiff = (float) (WorldConstraints.contOnContHeight * (Math.pow(Math.E, (- WorldConstraints.contOnContSteep * dist))));
				}else if (this.getPlate().isContinental() && !P.isContinental()) {
					// mountains set back a bit
					heightDiff = (float) (WorldConstraints.contOnOceanHeight * Math.pow(Math.E, - WorldConstraints.contOnOceanSteep * dist)/ (WorldConstraints.contOnOceanSteep * dist + WorldConstraints.contOnOceanSetback) );
				}else if (!this.getPlate().isContinental() && P.isContinental()) {
					// deep ocean trench, slowly levels off
					heightDiff = (float) ( - WorldConstraints.oceanOnContHeight * Math.pow(Math.E, (-WorldConstraints.oceanOnContSteep * dist)));
				}else {
					if (magnitude(this.getPlate().getDirection()) < magnitude (P.getDirection())) {
						// deep ocean trench
						heightDiff = (float) ( - WorldConstraints.oceanOnOceanHeight * Math.pow(Math.E, (-WorldConstraints.oceanOnOceanSteep * dist)));
					}else {
						// island arc
						heightDiff = (float) ( (WorldConstraints.islandArcHeight * Math.pow(Math.E, - WorldConstraints.islandArcSteep * dist)) / (Math.pow(WorldConstraints.islandArcSteep * dist, WorldConstraints.islandArcSteep * dist + WorldConstraints.islandArcSetback)) );
					}
				}

			} else { // plates moving apart

				if (this.getPlate().isContinental() && P.isContinental()) {
					// rift valley
					heightDiff = (float) (WorldConstraints.riftValleyHeight * (Math.pow((dist/ WorldConstraints.riftValleySetback) , (WorldConstraints.riftValleySetback / dist - WorldConstraints.riftValleySteep)) - WorldConstraints.riftValleyBase/(dist+1)));
				}else if (this.getPlate().isContinental() && !P.isContinental()) {
					// bit of a dip, then mountains up
					heightDiff = (float) (WorldConstraints.contFromOceanHeight * Math.pow(Math.E, - WorldConstraints.contFromOceanSteep * dist)/ (WorldConstraints.contFromOceanSteep * dist + WorldConstraints.contFromOceanSetback) );
				}else if (!this.getPlate().isContinental() && P.isContinental()) {
					// like the last one, but smaller and further from boundary
					heightDiff = (float) (WorldConstraints.oceanFromContHeight * Math.pow(Math.E, - WorldConstraints.oceanFromContSteep * dist)/ (WorldConstraints.oceanFromContSteep * dist + WorldConstraints.oceanFromContSetback) );
				}else {
					// mid-oceanic ridge
					heightDiff = (float)(WorldConstraints.oceanRidgeHeight/((WorldConstraints.oceanRidgeSteep * dist * dist + 1)));
					
				}

			}
			
			heightDiff = heightDiff + heightDiff * rand.nextFloat();

			// calc weighting based on dist and rel speed

			float weight = (float) relSpeed / (dist * dist);

			// add to list

			Float[] arr = { heightDiff, weight };
			tectonicEffects.add(arr);

		}

		// implement elevations based on weightings

		float heightSum = 0;
		float weightSum = 0;
		for (Float[] f : tectonicEffects) {
			heightSum = heightSum + f[0] * f[1];
			weightSum = weightSum + f[1];
		}

		this.height = this.height + heightSum / weightSum;
		//this.height = this.height + this.getPlate().getTilt(this.getCentroid());

	}

	
	public float relSpeed(Vector2f v1, Vector2f v2) {
		return (float) (Math.sqrt((v1.x - v2.x) * (v1.x - v2.x) + (v1.y - v2.y) * (v1.y - v2.y)));
	}

	private boolean movingCloser(Plate p1, Plate p2) {
		if (dotProd(p1.getDirection(), p2.getDirection()) > 0) {
			return true;
		}
		return false;
	}

	private static float magnitude(Vector2f v) {
		return ((float) Math.sqrt(v.x * v.x + v.y * v.y));
	}

	public static float dotProd(Vector2f v1, Vector2f v2) {
		return (v1.x * v2.x + v1.y * v2.y);
	}

	public void addEdge(Edge e) {
		this.edges.add(e);
	}

	public void addAdjacency(Polygon p) {
		this.adjacencies.add(p);
	}

	public boolean isInPlate() {
		return this.inPlate;
	}

	public void setInPlate(boolean b) {
		this.inPlate = b;
	}

	public float getHeight() {
		return this.height;
	}

	public void setHeight(float height) {
		this.height = height;
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
		// System.out.println("Setting plate");
		this.plate = plate;
		if (this.plate.isContinental()) {
			this.height = WorldConstraints.continentalBase;
		} else {
			this.height = WorldConstraints.oceanicBase;
		}
	}

	public boolean isChecked() {
		return this.checked;
	}

	public void uncheck() {
		this.checked = false;
	}

}