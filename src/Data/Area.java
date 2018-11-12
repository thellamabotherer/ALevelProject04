package Data;

import java.util.ArrayList;

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
	private float humidity;
	private float precipitation;
	private Vector2f currents;

	private boolean comparingOcean = true;

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
			this.currents = new Vector2f((float) 0, (float) 0);
			return;
		}
		this.ocean = false;
	}

	public void setupPrefs() {
		if (this.getLatitude() > WorldConstraints.HEIGHT / 2) { // northern hemisphere
			float weight = 0;
			if (ocean) {
				for (Area a : this.adjacencies) {
					if (a.ocean) {
						if (this.getOceanTemp() > a.getOceanTemp()) {
							if (this.getLongditude() < a.getLongditude()) {
								weight = (float) (weight + (this.getOceanTemp() - a.getOceanTemp() + 0.01) * 4);
							}else {
								weight = (float) (weight + (this.getOceanTemp() - a.getOceanTemp() + 0.01) * (float)0.25);
							}
						}
					}
				}
				if (weight > 0) {
					this.oceanPrefs = new ArrayList();
					for (Area a : this.adjacencies) {

						if (a.isOcean() && this.getOceanTemp() > a.getOceanTemp()) {
							if (this.getLongditude() < a.getLongditude()) {
								this.oceanPrefs.add(((this.getOceanTemp() - a.getOceanTemp() + (float)0.01) * 4)/weight);
							}else {
								this.oceanPrefs.add(((this.getOceanTemp() - a.getOceanTemp() + (float)0.01) * (float)0.25)/weight);
							}
						} else {
							this.oceanPrefs.add((float) 0);
						}
					}
				} else {
					this.oceanPrefs = null;
				}
			}
			weight = 0;
			for (Area a : this.adjacencies) {

				if (this.getAirTemp() > a.getAirTemp()) {
					weight = (float) (weight + (this.getAirTemp() - a.getAirTemp() + 0.01));
				}

			}
			if (weight > 0) {
				this.airPrefs = new ArrayList();
				for (Area a : this.adjacencies) {

					if (this.getAirTemp() > a.getAirTemp()) {
						this.airPrefs.add((this.airTemp - a.getAirTemp() + (float)0.01) / weight);
					} else {
						this.airPrefs.add((float) 0);
					}
				}
			} else {
				this.airPrefs = null;
			}
		} else { // southern hemisphere
			float weight = 0;
			if (ocean) {
				for (Area a : this.adjacencies) {
					if (a.ocean) {
						if (this.getOceanTemp() > a.getOceanTemp()) {
							if (this.getLongditude() > a.getLongditude()) {
								weight = weight + (this.getOceanTemp() - a.getOceanTemp()) * 4;
							}else {
								weight = weight + (this.getOceanTemp() - a.getOceanTemp()) * (float)0.25;
							}
						}
					}
				}
				if (weight > 0) {
					this.oceanPrefs = new ArrayList();
					for (Area a : this.adjacencies) {

						if (a.isOcean() && this.getOceanTemp() > a.getOceanTemp()) {
							if (this.getLongditude() > a.getLongditude()) {
								this.oceanPrefs.add(((this.getOceanTemp() - a.getOceanTemp()) * 4)/weight);
							}else {
								this.oceanPrefs.add(((this.getOceanTemp() - a.getOceanTemp()) * (float)0.25)/weight);
							}
						} else {
							this.oceanPrefs.add((float) 0);
						}
					}
				} else {
					this.oceanPrefs = null;
				}
			}
			weight = 0;
			for (Area a : this.adjacencies) {

				if (this.getAirTemp() > a.getAirTemp()) {
					weight = weight + (this.getAirTemp() - a.getAirTemp());
				}

			}
			if (weight > 0) {
				this.airPrefs = new ArrayList();
				for (Area a : this.adjacencies) {

					if (this.getAirTemp() > a.getAirTemp()) {
						this.airPrefs.add((this.airTemp - a.getAirTemp()) / weight);
					} else {
						this.airPrefs.add((float) 0);
					}
				}
			} else {
				this.airPrefs = null;
			}
		}
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

	public void getStartHumid() {
		if (this.ocean) {
			this.humidity = (2 * this.oceanTemp + 1) / 3;
			return;
		}
		this.humidity = 0;
	}

	public void getStartAirTemp() {
		if (this.ocean) {
			this.airTemp = (this.oceanTemp
					+ (1 - 2 * Math.abs(this.latitude - (WorldConstraints.HEIGHT / 2)) / WorldConstraints.HEIGHT)) / 2;
			return;
		}
		this.airTemp = (1 - 2 * Math.abs(this.latitude - (WorldConstraints.HEIGHT / 2)) / WorldConstraints.HEIGHT);
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

	public Vector2f getCurrents() {
		return currents;
	}

	public void setCurrents(Vector2f currents) {
		this.currents = currents;
	}

	public ArrayList<Float> getOceanPrefs() {
		return this.oceanPrefs;
	}

}
