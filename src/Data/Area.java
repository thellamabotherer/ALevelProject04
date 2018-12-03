package Data;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Main.Window;
import Main.WorldConstraints;

public class Area implements Comparable<Area> { // basically the poly from last time but with all the stuff with don't
												// need
	// anymore removed

	private float altitude;
	private float latitude;
	private float longditude;
	private Polygon poly;

	private boolean ocean;
	private boolean lake;

	private float oceanTemp;
	private float airTemp;
	private float water;

	private Area nextOcean;
	private Area nextAir;

	private boolean comparingOcean = true;

	private Vector2f currents;
	private Vector2f winds;

	private Vector4f colour;

	private ArrayList<Area> adjacencies;
	private ArrayList<Float> airPrefs;
	private ArrayList<Float> oceanPrefs;

	private float tempTemp;
	
	public Area(float altitude, float latitude, float longditude, Polygon poly) {
		this.altitude = altitude;
		this.latitude = latitude;
		this.longditude = longditude;
		this.poly = poly;
		if (this.altitude < 0) {
			this.ocean = true;
			return;
		}
		this.ocean = false;
	}

	public int compareTo(Area a) {
		if (comparingOcean) {
			if (this.oceanTemp == a.getOceanTemp()) {
				return 0;
			}
			if (this.oceanTemp > a.oceanTemp) {
				return -1;
			}
			return 1;
		}
		if (this.airTemp == a.getAirTemp()) {
			return 0;
		}
		if (this.airTemp > a.getAirTemp()) {
			return -1;
		}
		return 1;
	}

	public void setupAdjacencies() {
		this.adjacencies = new ArrayList();
		for (Polygon p : this.poly.getAdjacencies()) {
			this.adjacencies.add(p.getArea());
		}
	}

	public void getCurrentVect(WeatherSystem[] epicentres) {

		float weight = 0;
		float tempVectX;
		float tempVectY;
		float vectX = 0;
		float vectY = 0;
		float relX;
		float relY;

		for (WeatherSystem e : epicentres) {
			// get vector from here to e
			relX = this.longditude - e.getCoords().x;
			relY = this.latitude - e.getCoords().y;
			// get perp vector
			tempVectX = relY;
			tempVectY = -relX;
			// reduce perp vect to unit vect
			relX = (float) Math.sqrt(tempVectX * tempVectX + tempVectY * tempVectY);
			tempVectX = tempVectX / relX;
			tempVectY = tempVectY / relX;

			if (this.longditude > e.getCoords().x) {
				if (e.getSpin() == e.clockwise) {
					tempVectY = Math.abs(tempVectY);
				}else {
					tempVectY = -Math.abs(tempVectY);
				}
			}else {
				if (e.getSpin() != e.clockwise) {
					tempVectY = Math.abs(tempVectY);
				}else {
					tempVectY = -Math.abs(tempVectY);
				}
			}
			
			if (this.latitude < e.getCoords().y) {
				if (e.getSpin() == e.clockwise) {
					tempVectX = Math.abs(tempVectX);
				}else {
					tempVectX = -Math.abs(tempVectX);
				}
			}else {
				if (e.getSpin() != e.clockwise) {
					tempVectX = Math.abs(tempVectX);
				}else {
					tempVectX = -Math.abs(tempVectX);
				}
			}
			
			float tempWeight = (float) (Math.sqrt((double) ((this.longditude - e.getCoords().x) * (this.longditude - e.getCoords().x)
							+ (this.latitude - e.getCoords().y) * (this.latitude - e.getCoords().y))));
			weight = weight + tempWeight;

			// mutliply unit vect by weight * value >1 from WC

			tempVectX = tempVectX / tempWeight;
			tempVectY = tempVectY / tempWeight;

			// add vect to the total vects

			vectX = vectX + tempVectX;
			vectY = vectY + tempVectY;
			
		}

		vectX = 10 * vectX / weight;
		vectY = 10 * vectY / weight;

		this.currents = new Vector2f(vectX, vectY);

		// divide total vects by the overall weight

	}

	public void getWindVect(WeatherSystem[] epicentres) {

		float weight = 0;
		float tempVectX;
		float tempVectY;
		float vectX = 0;
		float vectY = 0;
		float relX;
		float relY;

		for (WeatherSystem e : epicentres) {
			// get vector from here to e
			relX = this.longditude - e.getCoords().x;
			relY = this.latitude - e.getCoords().y;
			// get perp vector
			tempVectX = relY;
			tempVectY = -relX;
			// reduce perp vect to unit vect
			relX = (float) Math.sqrt(tempVectX * tempVectX + tempVectY * tempVectY);
			tempVectX = tempVectX / relX;
			tempVectY = tempVectY / relX;

			if (this.longditude > e.getCoords().x) {
				if (e.getSpin() == e.clockwise) {
					tempVectY = Math.abs(tempVectY);
				}else {
					tempVectY = -Math.abs(tempVectY);
				}
			}else {
				if (e.getSpin() != e.clockwise) {
					tempVectY = Math.abs(tempVectY);
				}else {
					tempVectY = -Math.abs(tempVectY);
				}
			}
			
			if (this.latitude > e.getCoords().y) {
				if (e.getSpin() == e.clockwise) {
					tempVectX = Math.abs(tempVectX);
				}else {
					tempVectX = -Math.abs(tempVectX);
				}
			}else {
				if (e.getSpin() != e.clockwise) {
					tempVectX = Math.abs(tempVectX);
				}else {
					tempVectX = -Math.abs(tempVectX);
				}
			}
			
			float tempWeight = (float) (Math.sqrt((double) ((this.longditude - e.getCoords().x) * (this.longditude - e.getCoords().x)
							+ (this.latitude - e.getCoords().y) * (this.latitude - e.getCoords().y))));
			weight = weight + tempWeight;

			// mutliply unit vect by weight * value >1 from WC

			tempVectX = tempVectX / tempWeight;
			tempVectY = tempVectY / tempWeight;

			// add vect to the total vects

			vectX = vectX + tempVectX;
			vectY = vectY + tempVectY;
			
		}

		vectX = 10 * vectX / weight;
		vectY = 10 * vectY / weight;

		this.winds = new Vector2f(vectX, vectY);

	}

	public Area findBestNext(boolean type) { // true for current, false for wind
		
		if (type) { // then current
			for (Edge e : this.getPoly().getEdges()) {
				if (intersects(this.currents, e)) {
					if (this.getPoly() == e.getLeftSite().getPoly()) {
						return e.getRightSite().getPoly().getArea();
					}else {
						return e.getLeftSite().getPoly().getArea();
					}
				}
			}
		}else {
			for (Edge e : this.getPoly().getEdges()) {
				if (intersects(this.winds, e)) {
					if (this.getPoly() == e.getLeftSite().getPoly()) {
						return e.getRightSite().getPoly().getArea();
					}else {
						return e.getLeftSite().getPoly().getArea();
					}
				}
			}
		}
		return null;

	}public void setupNext (boolean type) {
		if (type) {
			if (this.isOcean()) {
				this.nextOcean = this.findBestNext(true);
			}
		}else {
			this.nextAir = this.findBestNext(false);
		}
	}

	public boolean intersects (Vector2f vect, Edge e) {

		// get y = mx + c  for edge 
		float mE = (float) ((e.getEnd().y - e.getStart().y) / (e.getEnd().x - e.getStart().x));
		float cE = (float) (e.getStart().y - mE * e.getStart().x);
		float mV = vect.y/vect.x;
		float cV = this.getLatitude() - mV * this.getLongditude();
		float xInt = (cV - cE) / (mE - mV);
		if (xInt > e.getStart().x && xInt < e.getEnd().y || xInt < e.getStart().x && xInt > e.getEnd().y) {
			return true;
		}return false;
	}

	public void getStartConditions () {
		if (this.isOcean()) {
			this.getSeaConditions();
		}this.getAirConditions();
	}
	
	public void getSeaConditions () {
		this.oceanTemp = (1 - Math.abs(WorldConstraints.HEIGHT/2 - this.latitude) / WorldConstraints.HEIGHT)/2;
	}public void getAirConditions () {
		this.airTemp = (1 - Math.abs(WorldConstraints.HEIGHT/2 - this.latitude) / WorldConstraints.HEIGHT)/2;
	}
	
	
	public float dotProd(Vector2f a, Vector2f b) {
		return (a.x * b.x + a.y + b.y);
	}

	public float magnitude(Vector2f a, Vector2f b) {
		return (float) Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}

	// ---------------------------------getters and setters
	// ----------------------------------

	public float getAltitude() {
		return altitude;
	}

	public ArrayList<Float> getAirPrefs() {
		return airPrefs;
	}

	public void setAirPrefs(ArrayList<Float> airPrefs) {
		this.airPrefs = airPrefs;
	}

	public void setOceanPrefs(ArrayList<Float> oceanPrefs) {
		this.oceanPrefs = oceanPrefs;
	}

	public Vector4f getColour() {
		return colour;
	}

	public void setColour(Vector4f colour) {
		this.colour = colour;
	}

	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongditude() {
		return longditude;
	}

	public void setLongditude(float longditude) {
		this.longditude = longditude;
	}

	public boolean isOcean() {
		return ocean;
	}

	public void setOcean(boolean ocean) {
		this.ocean = ocean;
	}

	public float getOceanTemp() {
		return oceanTemp;
	}

	public void setOceanTemp(float oceanTemp) {
		this.oceanTemp = oceanTemp;
	}

	public float getAirTemp() {
		return airTemp;
	}

	public void setAirTemp(float airTemp) {
		this.airTemp = airTemp;
	}

	public ArrayList<Area> getAdjacencies() {
		return adjacencies;
	}

	public void setAdjacencies(ArrayList<Area> adjacencies) {
		this.adjacencies = adjacencies;
	}

	public ArrayList<Float> getOceanPrefs() {
		return this.oceanPrefs;
	}

	public Polygon getPoly() {
		return poly;
	}

	public void setPoly(Polygon poly) {
		this.poly = poly;
	}

	public Vector2f getCurrents() {
		return currents;
	}

	public void setCurrents(Vector2f currents) {
		this.currents = currents;
	}

	public Vector2f getWinds() {
		return winds;
	}

	public void setWinds(Vector2f winds) {
		this.winds = winds;
	}

	public Area getNextOcean() {
		return nextOcean;
	}

	public void setNextOcean(Area nextOcean) {
		this.nextOcean = nextOcean;
	}

	public Area getNextAir() {
		return nextAir;
	}

	public void setNextAir(Area nextAir) {
		this.nextAir = nextAir;
	}

	public float getWater() {
		return water;
	}

	public void setWater(float water) {
		this.water = water;
	}

	public float getTempTemp() {
		return tempTemp;
	}

	public void setTempTemp(float tempTemp) {
		this.tempTemp = tempTemp;
	}
	
	

}