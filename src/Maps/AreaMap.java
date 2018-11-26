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
		double start;
		double end;
		this.HR = hr;
		start = System.nanoTime();
		for (Plate P : heightMap.getPlates()) {
			for (Polygon p : P.getPolys()) {
				areas.add(p.toArea());
			}
		}end = System.nanoTime();
		System.out.println("Setup area map takes " + (end - start));
		start = System.nanoTime();
		for (Area a : areas) {
			a.setupAdjacencies();
		}end = System.nanoTime();
		System.out.println("Setup adj takes " + (end - start));
		this.getStartValues();
		start = System.nanoTime();
		this.simWeather();
		end = System.nanoTime();
		System.out.println("Weather sim 	takes " + (end - start));
		for (Area a : areas) {
			a.getSeaConditions();
		}
	}

	private void getStartValues() {

		// for each area

		// if ocean
		// water genned from temp

		// create weather obj (cloud subclass)

	}

	private void simWeather() {

		epicentres = genCentres(WorldConstraints.seaCentres, true);
		for (Area a : this.areas) {
			if (a.isOcean()) {
				a.getCurrentVect(epicentres);
			}
		}epicentres = genCentres(WorldConstraints.seaCentres, false);
		for (Area a : this.getAreas()) {
			a.getWindVect(epicentres);
		}

		// walk this area's weather object from here and deposit heat and moisture based
		// on change in altitude until weather is depleted

	}private WeatherSystem[] genCentres (int num, boolean sea) {
		Random rand = new Random();
		int temp;
		WeatherSystem[] epicentres = new WeatherSystem[num];
		for (int i = 0; i < num; i++) {
			WeatherSystem epicentre = null;
			while (epicentre == null) {
				temp = rand.nextInt(this.areas.size());
				if ((this.areas.get(temp).isOcean() || !sea) && this.areas.get(temp).getLatitude() > WorldConstraints.HEIGHT * 0.3 && this.areas.get(temp).getLatitude() < WorldConstraints.HEIGHT * 0.7) {
					epicentre = new WeatherSystem(WeatherSystem.air, this.areas.get(temp));
				}
			}
			epicentres[i] = epicentre;
		}this.epicentres = epicentres;
		return epicentres;
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