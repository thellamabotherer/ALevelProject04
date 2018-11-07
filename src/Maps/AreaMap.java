package Maps;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;

import Data.Area;
import Data.Plate;
import Data.Polygon;
import Main.Window;

public class AreaMap {
	
	private ArrayList<Area> areas = new ArrayList();
	
	public AreaMap (HeightMap heightMap) {
		for (Plate P : heightMap.getPlates()) {
			for (Polygon p : P.getPolys()) {
				areas.add(p.toArea());
			}
		}
		for (Area a : areas) {
			a.setupAdjacencies();
		}
		this.simulateOceanCurrents();
		for (Area a : this.areas) {
			a.getStartHumid();
			a.getStartAirTemp();
		}
	}
	
	public void simulateOceanCurrents () {

		PriorityQueue<Area> pQueue = new PriorityQueue(10000);
		for (Area a : this.areas) {
			if (a.isOcean()) {
				pQueue.add(a);
			}
		}
		while (!pQueue.isEmpty()) {
			Area a = pQueue.remove();
			a.simulateCurrents();
		}
	}
	
	
	
	//----------------------- graphical stuff :( ----------------------------------
	
	public void draw (Window w, int mode) { // 0 = centroid, 1 = wireframe, 2 = plateMap, 3 = heightMap, 4 = simpleTerrain, 5 = oceanTemp, 6 = airTemp, 7 = humidity
		for (Area a : areas) {
			a.draw (w, mode);
		}
	}

}
