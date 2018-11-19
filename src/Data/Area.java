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
	private float oceanEnergy;

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

	public Area(float altitude, float latitude, float longditude, Polygon poly) {
		this.altitude = altitude;
		this.latitude = latitude;
		this.longditude = longditude;
		this.poly = poly;
		if (this.altitude < 0) {
			this.ocean = true;
			this.oceanEnergy = (1
					- 2 * Math.abs(this.latitude - (WorldConstraints.HEIGHT / 2)) / WorldConstraints.HEIGHT)
					* (-this.altitude);
			this.oceanTemp = this.oceanEnergy / (-this.altitude);
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
			tempVectY = - relX;
			// reduce perp vect to unit vect
			relX = (float) Math.sqrt(tempVectX * tempVectX + tempVectY * tempVectY);
			tempVectX = tempVectX / relX;
			tempVectY = tempVectY / relX;
			// if on left 
			if (this.longditude < e.getCoords().x && e.getSpin() == WeatherSystem.clockwise) {
				// make unit vect negative if need to for cw or ccw purposes
				tempVectY = Math.abs(tempVectY);
			}else if (this.longditude < e.getCoords().x && e.getSpin() != WeatherSystem.clockwise){
				tempVectY = -Math.abs(tempVectY);
			}else if (this.longditude > e.getCoords().x && e.getSpin() == WeatherSystem.clockwise) {
				tempVectY = -Math.abs(tempVectY);
			}else {
				tempVectY = Math.abs(tempVectY);
			}if (this.longditude < e.getCoords().x && e.getSpin() == WeatherSystem.clockwise) {
				// make unit vect negative if need to for cw or ccw purposes
				tempVectY = Math.abs(tempVectY);
			}else if (this.longditude < e.getCoords().x && e.getSpin() != WeatherSystem.clockwise){
				tempVectY = -Math.abs(tempVectY);
			}else if (this.longditude > e.getCoords().x && e.getSpin() == WeatherSystem.clockwise) {
				tempVectY = -Math.abs(tempVectY);
			}else {
				tempVectY = Math.abs(tempVectY);
			}
			
			// find weight based on dist to e 
			// mutliply unit vect by weight * value >1 from WC
			// add vect to the total vects 
			// add weight to the total weight 
		}// divide total vects by the overall weight
		
	}public void findBestNext () {
		
	}
	
	
	public float dotProd(Vector2f a, Vector2f b) {
		return (a.x * b.x + a.y + b.y);
	}

	public float magnitude(Vector2f a, Vector2f b) {
		return (float) Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}

	// ----------------------- graphical stuff :( ----------------------------------

	public void draw(Window w, int mode) { // 0 = centroid, 1 = wireframe, 2 = plateMap, 3 = heightMap, 4 =
											// simpleTerrain, 5 = oceanTemp, 6 = airTemp, 7 = humidity
		w.beginRender();
		if (mode == 0) {
			w.changeColour(new Vector4f(this.oceanEnergy, 0, 0, 1));
		} else if (mode == 1) {
			w.changeColour(new Vector4f(this.oceanTemp, 0, 0, 1));
		} else if (mode == 2) {
			w.changeColour(new Vector4f(this.airTemp, 0, 0, 1));
			w.addVertex(new Vector3f(this.longditude + 5, this.latitude, 0));
			w.addVertex(new Vector3f(this.longditude, this.latitude + 5, 0));
			w.addVertex(new Vector3f(this.longditude - 5, this.latitude, 0));
			w.endRender();
			w.changeColour(new Vector4f(0, this.oceanTemp, 0, 1));
			w.beginRender();
			w.addVertex(new Vector3f(this.longditude + 5, this.latitude, 0));
			w.addVertex(new Vector3f(this.longditude, this.latitude - 5, 0));
			w.addVertex(new Vector3f(this.longditude - 5, this.latitude, 0));
			w.endRender();
			return;
		}
		w.addVertex(new Vector3f(this.longditude + 10, this.latitude, 0));
		w.addVertex(new Vector3f(this.longditude, this.latitude + 10, 0));
		w.addVertex(new Vector3f(this.longditude - 10, this.latitude, 0));
		w.addVertex(new Vector3f(this.longditude, this.latitude - 10, 0));
		w.endRender();
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

	public float getOceanEnergy() {
		return oceanEnergy;
	}

	public void setOceanEnergy(float oceanEnergy) {
		this.oceanEnergy = oceanEnergy;
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
	
	

}