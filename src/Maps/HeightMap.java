package Maps;

import java.util.ArrayList;

import Data.Plate;
import Data.Polygon;

public class HeightMap {
	
	private ArrayList<Plate> plates;
	private float maxHeight = Float.MIN_VALUE;
	private float minHeight = Float.MAX_VALUE;
	
	public HeightMap (ArrayList<Plate> Plates) {
		
		this.plates = Plates;
		
		for (Plate P : this.plates) {
			for (Polygon p : P.getPolys()) {
				p.checkEdge();
			}P.findAdj();
			for (Polygon p : P.getPolys()) {
				p.calculateElevation();
			}
			if (!P.isContinental()) {
				for (Polygon p : P.getPolys()) {
					p.smoothIsland();
				}
			}
		}normaliseHeights();
		
	}public ArrayList<Plate> getPlates () {
		return this.plates;
	}
	
	private void normaliseHeights () {
		for (Plate P : this.plates) {
			for (Polygon p : P.getPolys()) {
				if (p.getHeight() > this.maxHeight) {
					this.maxHeight = p.getHeight();
				}if (p.getHeight() < this.minHeight) {
					this.minHeight = p.getHeight();
				}
			}
		}float range = this.maxHeight - this.minHeight;
		System.out.println(minHeight);
		for (Plate P : this.plates) {
			for (Polygon p : P.getPolys()) {
				p.setHeight(((p.getHeight() - minHeight)/(range / 2) - 1));
			}
		}
	}

}