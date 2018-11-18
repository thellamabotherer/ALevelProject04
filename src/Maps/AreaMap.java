package Maps;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

import org.lwjgl.opengl.Display;

import java.util.Comparator;

import Data.Area;
import Data.Cloud;
import Data.Current;
import Data.Plate;
import Data.Polygon;
import Data.Weather;
import Main.Main;
import Main.Window;
import Main.WorldConstraints;
import Renderers.HeightRenderer;

public class AreaMap {

	private ArrayList<Area> areas = new ArrayList();
	private HeightRenderer HR;

	public AreaMap(HeightMap heightMap, Window w, HeightRenderer hr) {
		this.HR = hr;
		for (Plate P : heightMap.getPlates()) {
			for (Polygon p : P.getPolys()) {
				areas.add(p.toArea());
			}
		}
		for (Area a : areas) {
			a.setupAdjacencies();
			a.getStartHumid();
			a.getStartAirTemp();
		}
		for (Area a : this.areas) {
			a.setupPrefs();
		}this.simWeather();
	}

	private void simWeather () {
		Random rand = new Random();
		Weather c;
		for (int i = 0; i < WorldConstraints.currents; i++) {
			c = new Current (this.areas.get(rand.nextInt(this.areas.size())));
			//Main.heightRenderer.drawSimpleTerrain();
			c.walk();
		}for (int i = 0; i < WorldConstraints.currents; i++) {
			c = new Cloud (this.areas.get(rand.nextInt(this.areas.size())));
			//Main.heightRenderer.drawSimpleTerrain();
			c.walk();
		}
	}

	// ----------------------- graphical stuff :( ----------------------------------

	public void draw(Window w, int mode) { // 0 = centroid, 1 = wireframe, 2 = plateMap, 3 = heightMap, 4 =
											// simpleTerrain, 5 = oceanTemp, 6 = airTemp, 7 = humidity
		for (Area a : areas) {
			a.draw(w, mode);
		}
	}

}
