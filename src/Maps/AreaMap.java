package Maps;

import java.util.ArrayList;
import java.util.PriorityQueue;

import org.lwjgl.opengl.Display;

import java.util.Comparator;

import Data.Area;
import Data.Plate;
import Data.Polygon;
import Main.Window;
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
		}
		this.simulateOceanCurrents(w);
		for (Area a : this.areas) {
			a.getStartHumid();
			a.getStartAirTemp();
		}
	}

	public void simulateOceanCurrents(Window w) {

		PriorityQueue<Area> pQueue = new PriorityQueue(10000);
		for (Area a : this.areas) {
			if (a.isOcean()) {
				pQueue.add(a);
			}
		}
		int num = 50;
		while (!pQueue.isEmpty()) {
			num--;
			Area a = pQueue.remove();
			a.simulateCurrents();
			if (num == 0) {
				this.HR.drawSimpleTerrain();
				this.draw(w, 1);
				Display.update();
				num = 50;
			}
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
