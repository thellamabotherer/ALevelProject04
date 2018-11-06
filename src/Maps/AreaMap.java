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
	}
	
	public void simulateOceanCurrents () {

		PriorityQueue<Area> pQueue = new PriorityQueue(10000, Comparator<oceanTemp>);
		
		// run diffusion based method many times on all areas 
	}
	
	
	
	//----------------------- graphical stuff :( ----------------------------------
	
	public void draw (Window w, int mode) { // 0 = centroid, 1 = wireframe, 2 = plateMap, 3 = heightMap, 4 = simpleTerrain, 5 = oceanTemp, 6 = airTemp, 7 = humidity
		for (Area a : areas) {
			a.draw (w, mode);
		}
	}

}
