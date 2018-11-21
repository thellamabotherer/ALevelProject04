package Maps;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

import org.lwjgl.opengl.Display;

import java.util.Comparator;

import Data.Area;
import Data.Plate;
import Data.Polygon;
import Data.WeatherSystem;
import Main.Main;
import Main.Window;
import Main.WorldConstraints;
import Renderers.HeightRenderer;

public class AreaMap {

	private ArrayList<Area> areas = new ArrayList();
	private HeightRenderer HR;
	
	private WeatherSystem[] epicentres;

	public AreaMap(HeightMap heightMap, Window w, HeightRenderer hr) {
		this.HR = hr;
		for (Plate P : heightMap.getPlates()) {
			for (Polygon p : P.getPolys()) {
				areas.add(p.toArea());
			}
		}
		for (Area a : areas) {
			a.setupAdjacencies();
		}
		this.simCurrents();
		this.getStartValues();
		this.simWeather();
	}

	private void simCurrents() {
		Random rand = new Random();
		int numCCentres;
		int temp;
		if (WorldConstraints.currentCentresMin < WorldConstraints.currentCentresMax) {
			numCCentres = WorldConstraints.currentCentresMin
					+ rand.nextInt(WorldConstraints.currentCentresMax - WorldConstraints.currentCentresMin);
		} else {
			numCCentres = WorldConstraints.currentCentresMin;
		}
		WeatherSystem[] epicentres = new WeatherSystem[numCCentres];
		for (int i = 0; i < numCCentres; i++) {
			WeatherSystem epicentre = null;
			while (epicentre == null) {
				temp = rand.nextInt(this.areas.size());
				if (this.areas.get(temp).isOcean() && this.areas.get(temp).getLatitude() > WorldConstraints.HEIGHT * 0.3 && this.areas.get(temp).getLatitude() < WorldConstraints.HEIGHT * 0.7) {
					epicentre = new WeatherSystem(WeatherSystem.current, this.areas.get(temp));
				}
			}
			epicentres[i] = epicentre;
		}	
		this.epicentres = epicentres;
		for (Area a : this.areas) {
			if (a.isOcean()) {
				a.getCurrentVect(epicentres);
			}
		}
		// move thermal energy around based off the currents

		// for each area, make weather object (current subclass)
		// set current object to walk the nodes until it has deposited all of it's
		// energy
		// if current hits coast, flood out remaining energy

	}

	private void getStartValues() {

		// for each area

		// if ocean
		// water genned from temp

		// create weather obj (cloud subclass)

	}

	private void simWeather() {

		Random rand = new Random();
		// generate a few weather centres
		int numCCentres;
		int temp;
		if (WorldConstraints.airCentresMin < WorldConstraints.airCentresMax) {
			numCCentres = WorldConstraints.airCentresMin
					+ rand.nextInt(WorldConstraints.airCentresMax - WorldConstraints.airCentresMin);
		} else {
			numCCentres = WorldConstraints.airCentresMin;
		}
		WeatherSystem[] epicentres = new WeatherSystem[numCCentres];
		for (int i = 0; i < numCCentres; i++) {
			WeatherSystem epicentre = null;
			while (epicentre == null) {
				temp = rand.nextInt(this.areas.size());
				if (this.areas.get(temp).isOcean() && this.areas.get(temp).getLatitude() > WorldConstraints.HEIGHT * 0.3 && this.areas.get(temp).getLatitude() < WorldConstraints.HEIGHT * 0.7) {
					epicentre = new WeatherSystem(WeatherSystem.air, this.areas.get(temp));
				}
			}
			epicentres[i] = epicentre;
		}this.epicentres = epicentres;

		for (Area a : this.areas) {

			// for each weather epicentre, gen a vector perpendicular to the line between
			// this and the epicentre

			a.getCurrentVect(epicentres);

			// align this in the correct direction based on how far around it we are
			// divide the vector by a scalar function of the distance between this and the
			// epicentre
			// sum up all of these vectors to get a prevailing current direction

			// all done in method in area

		}

		// walk this area's weather object from here and deposit heat and moisture based
		// on change in altitude until weather is depleted

	}

	// ----------------------- graphical stuff :( ----------------------------------

	public void draw(Window w, int mode) { // 0 = centroid, 1 = wireframe, 2 = plateMap, 3 = heightMap, 4 =
											// simpleTerrain, 5 = oceanTemp, 6 = airTemp, 7 = humidity
		for (Area a : areas) {
			a.draw(w, mode);
		}
	}

	public ArrayList<Area> getAreas() {
		return areas;
	}

	public void setAreas(ArrayList<Area> areas) {
		this.areas = areas;
	}

	public WeatherSystem[] getEpicentres() {
		return epicentres;
	}

	public void setEpicentres(WeatherSystem[] epicentres) {
		this.epicentres = epicentres;
	}

	// --------------------------
	
	

}