package Data;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Main.Window;
import Main.WorldConstraints;

public class Area { // basically the poly from last time but with all the stuff with don't need
					// anymore removed

	private float altitude;
	private float latitude;
	private float longditude;
	private Polygon poly;

	private boolean ocean;
	private float oceanEnergy;

	private float oceanTemp;
	private float airTemp;
	private float humidity;
	private float precipitation;

	private ArrayList<Area> adjacencies;

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
			return;
		}
		this.ocean = false;
	}

	public void setupAdjacencies() {
		this.adjacencies = new ArrayList();
		for (Polygon p : this.poly.getAdjacencies()) {
			this.adjacencies.add(p.getArea());
		}
	}

	public void simulateCurrents() {
		float maxDiff = 0;
		Area a1 = null;
		float secondDiff = 0;
		Area a2 = null;
		float diff;
		for (Area a : this.adjacencies) {
			if (a.ocean) {
				diff = this.oceanTemp - a.getOceanTemp();
				if (diff > maxDiff) {
					secondDiff = maxDiff;
					maxDiff = diff;
					a2 = a1;
					a1 = a;
				} else if (diff > secondDiff) {
					secondDiff = diff;
					a2 = a;
				}
			}
		}
		if (a1 != null) {
			a1.setOceanEnergy(a1.getOceanTemp() - maxDiff * a1.getAltitude());
			a1.setOceanTemp(a1.getOceanTemp()/ (-1 * a1.getAltitude()));
			this.oceanEnergy = this.oceanEnergy + maxDiff * a1.getAltitude();
			this.oceanTemp = this.oceanEnergy/(-1 * this.altitude);
		}if (a2 != null) {
			a2.setOceanEnergy(a2.getOceanTemp() - secondDiff * a2.getAltitude());
			a2.setOceanTemp(a2.getOceanTemp()/ (-1 * a2.getAltitude()));
			this.oceanEnergy = this.oceanEnergy + secondDiff * a2.getAltitude();
			this.oceanTemp = this.oceanEnergy/(-1 * this.altitude);
		}
	}

	// ----------------------- graphical stuff :( ----------------------------------

	public void draw(Window w, int mode) { // 0 = centroid, 1 = wireframe, 2 = plateMap, 3 = heightMap, 4 =
											// simpleTerrain, 5 = oceanTemp, 6 = airTemp, 7 = humidity
		w.beginRender();
		w.changeColour(new Vector4f(this.oceanEnergy, 0, 0, 1));
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

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public ArrayList<Area> getAdjacencies() {
		return adjacencies;
	}

	public void setAdjacencies(ArrayList<Area> adjacencies) {
		this.adjacencies = adjacencies;
	}

}
