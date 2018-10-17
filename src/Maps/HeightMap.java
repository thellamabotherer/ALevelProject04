package Maps;

import java.util.ArrayList;

import Data.Plate;
import Data.Polygon;

public class HeightMap {
	
	private ArrayList<Plate> plates;
	
	public HeightMap (ArrayList<Plate> Plates) {
		
		this.plates = Plates;
		
		for (Plate P : this.plates) {
			for (Polygon p : P.getPolys()) {
				p.collide();
			}
		}
		
	}public ArrayList<Plate> getPlates () {
		return this.plates;
	}

}
