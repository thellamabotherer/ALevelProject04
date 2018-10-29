package Data;

import java.util.ArrayList;
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

	public float distToPoly (Polygon  P) {
		float dx = this.getCentroid().x - P.getCentroid().x;
		float dy = this.getCentroid().y - P.getCentroid().y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}
	
	public void checkEdge() {
		for (Edge e : this.edges) {
			if (e.getRightSite().getPoly().getPlate() != this.getPlate() || e.getLeftSite().getPoly().getPlate() != this.getPlate()) {
				this.edgeOfPlate = true;
				return;
			}
		}this.edgeOfPlate = false;
	}

	public boolean isEdgeOfPlate() {
		return edgeOfPlate;
	}
	
	public Object findNearestEdge (int a) {
		float min = Float.MAX_VALUE;
		Polygon activePoly = null;
		for (Polygon P : this.getPlate().getPolys()) {
			if (this.distToPoly(P) < min && P.isEdgeOfPlate() == true) {
				
				min = this.distToPoly(P);
				activePoly = P;
			}
		}
		if (a == 0) {return activePoly;}
		return min;
	}
	
	public void calculateElevation () {
		
		Random rand = new Random();
		
		Polygon nearestBoundary = (Polygon) this.findNearestEdge(0);
		if (nearestBoundary == null) {return;}
		
		float dist = (float) this.findNearestEdge(1);
		Plate otherPlate = null;
		
		// find type of nearest tectonic interaction 

		for (Polygon P : nearestBoundary.adjacencies) {
			if (nearestBoundary.getPlate() != P.getPlate()) {
				int type = movingTowards (nearestBoundary, P, P);
				otherPlate = P.getPlate();
			}
		}
		float relativeSpeed = (float) (
				Math.sqrt(
				(this.getPlate().getDirection().x - otherPlate.getDirection().x) *
				(this.getPlate().getDirection().x - otherPlate.getDirection().x) +
				(this.getPlate().getDirection().y - otherPlate.getDirection().y) *
				(this.getPlate().getDirection().y - otherPlate.getDirection().y)
				));

		if (dotProd (this.getPlate().getDirection(), otherPlate.getDirection()) < 0) { // subducting boundaries when plates going towards each other
		
		// for continent on continent
		if (this.getPlate().isContinental() && otherPlate.isContinental()) {
			// add mountains proportional to relative V and inversely proportional to dist ^ 2
			this.height = (float) (this.height + 
					WorldConstraints.continentOnContinentBase * 
					relativeSpeed * 
					Math.pow(Math.E, -(WorldConstraints.continentOnContinentSteep * dist)) +
					rand.nextFloat() * WorldConstraints.continentOnContinentRand
					);
		}// continent on ocean 
		else if (this.getPlate().isContinental() && !otherPlate.isContinental()) {
			// height in relation to |1 - log (x+3) - (2sin(dist))/(1+|x-2|)|
			this.height = (float) (this.height + 
					WorldConstraints.continentOnOceanBase *
					relativeSpeed * 
					Math.pow(Math.E, - Math.abs(dist * WorldConstraints.continentOnOceanSteep - WorldConstraints.continentOnOceanSetback) +
							rand.nextFloat() * WorldConstraints.continentOnOceanRand)
					);
		}// ocean on continent
		else if (!this.getPlate().isContinental() && otherPlate.isContinental()) {
			// height in relation to (4log(x+1/2) - 2)/(dist + 3)
			this.height = (float) (this.height + 
					WorldConstraints.oceanOnContinentBase *
					relativeSpeed * 
					(Math.log((dist * WorldConstraints.oceanOnContinentSteep + 1)/2) - 2 ) / (dist * WorldConstraints.oceanOnContinentSteep + 3) +
					rand.nextFloat() * WorldConstraints.oceanOnContinentRand
					);
					
		}// ocean on ocean 
		// same as continent on continent (kinda)
		else {
			this.height = (float) (this.height + 
					WorldConstraints.oceanOnOceanBase * 
					relativeSpeed * 
					Math.pow(Math.E, -(WorldConstraints.oceanOnOceanSteep * dist))+
					rand.nextFloat() * WorldConstraints.oceanOnOceanRand);
		}
		
		}else { // plates moving apart
			
		}
	}

	public void collide() { // I'm hoping to deprecate this method and replace it with something that looks a little nicer 

		// System.out.println("Collide method.");

		Random rand = new Random();

		for (Edge e : edges) {
			// check if eq of edge intersects with bigVect between getStart and getEnd

			if (movingTowards(this, e.getLeftSite().getPoly(), e.getRightSite().getPoly()) == 1) {
				// away from the leftsite

				System.out.println("Away 1");
				
				// if both same
				if (this.getPlate().isContinental() && e.getLeftSite().getPoly().getPlate().isContinental()) {
					// Rift Valley; Low, sharp depression. Radiate out some low non-steep mountains.
					// Medium volcanic activity.
					this.height = this.height + WorldConstraints.riftValleyLower
							+ rand.nextFloat() * (WorldConstraints.riftValleyHigher - WorldConstraints.riftValleyLower);

				} else if (!this.getPlate().isContinental() && !e.getLeftSite().getPoly().getPlate().isContinental()) {
					this.height = this.height + WorldConstraints.oceanTrenchLower + rand.nextFloat()
							* (WorldConstraints.oceanTrenchHigher - WorldConstraints.oceanTrenchLower);
				}

				// if this ocean and that continent
				else if (!this.getPlate().isContinental() && e.getLeftSite().getPoly().getPlate().isContinental()) {
					// Ocean trench, medium volcanic activity.
					this.height = this.height + WorldConstraints.oceanTrenchLower + rand.nextFloat()
							* (WorldConstraints.oceanTrenchHigher - WorldConstraints.oceanTrenchLower);
					;

				}

				// if this cont and that ocean
				else if (this.getPlate().isContinental() && !e.getLeftSite().getPoly().getPlate().isContinental()) {
					// Low mountains, high volcanic activity.
					this.height = this.height + WorldConstraints.smallMountainsLower + rand.nextFloat()
							* (WorldConstraints.smallMountainsHigher - WorldConstraints.smallMountainsLower);
				}

				else {
					System.out.println("Error in polygon, collide method.");
				}

			}
			if (movingTowards(this, e.getLeftSite().getPoly(), e.getRightSite().getPoly()) == 2) {
				// away from the rightSite

				System.out.println("Away 2");
				
				// if both same
				if (this.getPlate().isContinental() && e.getRightSite().getPoly().getPlate().isContinental()) {
					// Rift Valley; Low, sharp depression. Radiate out some low non-steep mountains.
					// Medium volcanic activity.
					this.height = this.height + WorldConstraints.riftValleyLower
							+ rand.nextFloat() * (WorldConstraints.riftValleyHigher - WorldConstraints.riftValleyLower);

				} else if (!this.getPlate().isContinental() && !e.getRightSite().getPoly().getPlate().isContinental()) {
					this.height = this.height + WorldConstraints.oceanTrenchLower + rand.nextFloat()
							* (WorldConstraints.oceanTrenchHigher - WorldConstraints.oceanTrenchLower);
				}

				// if this ocean and that continent
				else if (!this.getPlate().isContinental() && e.getRightSite().getPoly().getPlate().isContinental()) {
					// Ocean trench, medium volcanic activity.
					this.height = this.height + WorldConstraints.riftValleyLower
							+ rand.nextFloat() * (WorldConstraints.riftValleyHigher - WorldConstraints.riftValleyLower);
				}

				// if this cont and that ocean
				else if (this.getPlate().isContinental() && !e.getRightSite().getPoly().getPlate().isContinental()) {
					// Low mountains, high volcanic activity.
					this.height = this.height + WorldConstraints.smallMountainsLower + rand.nextFloat()
							* (WorldConstraints.smallMountainsHigher - WorldConstraints.smallMountainsLower);
				}

				else {
					System.out.println("Error in polygon, collide method.");
				}

			} else if (movingTowards(this, e.getLeftSite().getPoly(), e.getRightSite().getPoly()) == 11) {
				// towards leftSite

				System.out.println("Towards 1");

				// if both the same
				if ((this.getPlate().isContinental() && e.getLeftSite().getPoly().getPlate().isContinental()) || !(this.getPlate().isContinental() && !e.getLeftSite().getPoly().getPlate().isContinental())) {
					
					// send up high mountains
					
					this.height = this.height 
							+WorldConstraints.bigMountainsLower 
							+ rand.nextFloat() * (WorldConstraints.bigMountainsHigher - WorldConstraints.bigMountainsLower);
					
				}
				// if this continent and that ocean
				
				else if (this.getPlate().isContinental() && !e.getLeftSite().getPoly().getPlate().isContinental()) {
				// send up medium mountains

					this.height = this.height 
							+WorldConstraints.mediumMountainsLower 
							+ rand.nextFloat() * (WorldConstraints.mediumMountainsHigher - WorldConstraints.mediumMountainsLower);
					
					
				}	
				
				else {
				
				// if this ocean and that continent

					this.height = this.height 
							+WorldConstraints.oceanTrenchLower 
							+ rand.nextFloat() * (WorldConstraints.oceanTrenchHigher - WorldConstraints.oceanTrenchLower);
					
					
				// send down trench

				}	
					
			} else if (movingTowards(this, e.getLeftSite().getPoly(), e.getRightSite().getPoly()) == 12) {
				// towardsRightSite

				System.out.println("Towards 2");

				// if both the same
				if ((this.getPlate().isContinental() && e.getRightSite().getPoly().getPlate().isContinental()) || !(this.getPlate().isContinental() && !e.getRightSite().getPoly().getPlate().isContinental())) {
					
					// send up high mountains
					
					this.height = this.height 
							+WorldConstraints.bigMountainsLower 
							+ rand.nextFloat() * (WorldConstraints.bigMountainsHigher - WorldConstraints.bigMountainsLower);
					
				}
				// if this continent and that ocean
				
				else if (this.getPlate().isContinental() && !e.getRightSite().getPoly().getPlate().isContinental()) {
				// send up medium mountains

					this.height = this.height 
							+WorldConstraints.mediumMountainsLower 
							+ rand.nextFloat() * (WorldConstraints.mediumMountainsHigher - WorldConstraints.mediumMountainsLower);
					
					
				}	
				
				else {
				
				// if this ocean and that continent

					this.height = this.height 
							+WorldConstraints.oceanTrenchLower 
							+ rand.nextFloat() * (WorldConstraints.oceanTrenchHigher - WorldConstraints.oceanTrenchLower);
					
					
				// send down trench

				}	
			}

		}

	}

	private static boolean movingTowardsPoly(Polygon thisPoly, Polygon leftSite, Polygon rightSite) { // deprecated method 

		if (thisPoly == leftSite) {

			if (thisPoly.getPlate() == rightSite.getPlate()) {
				return false;
			}

			else {
				return true;
			}
		}

		if (thisPoly.getPlate() == leftSite.getPlate()) {
			return false;
		}

		return true;

	}
	
	private static int movingTowards(Polygon thisPoly, Polygon leftSite, Polygon rightSite) {
		if (thisPoly == leftSite) {
			
			if (thisPoly.getPlate() == rightSite.getPlate()) {return 0;}
			
			// run alg on right site
			Vector2f v = new Vector2f(thisPoly.getPlate().getDirection().x - rightSite.getPlate().getDirection().x,
					thisPoly.getPlate().getDirection().y - rightSite.getPlate().getDirection().y);
			Vector2f r = new Vector2f(thisPoly.getCentroid().x - rightSite.getCentroid().x,
					thisPoly.getCentroid().y - rightSite.getCentroid().y);
			if (dotProd(v, r) > 0) { // away from right
				return 2;
			} else { // towards right
				return 12;
			}

		} else {
			
			if (thisPoly.getPlate() == leftSite.getPlate()) {return 0;}
			
			// run alg on left site
			Vector2f v = new Vector2f(thisPoly.getPlate().getDirection().x - leftSite.getPlate().getDirection().x,
					thisPoly.getPlate().getDirection().y - leftSite.getPlate().getDirection().y);
			Vector2f r = new Vector2f(thisPoly.getCentroid().x - leftSite.getCentroid().x,
					thisPoly.getCentroid().y - leftSite.getCentroid().y);
			if (dotProd(v, r) > 0) { // away from right
				return 1;
			} else { // towards right
				return 11;
			}
		}

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

}