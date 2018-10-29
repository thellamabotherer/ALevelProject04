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
		double start;
		double end;
		
		for (Plate P : this.plates) {
			start = System.nanoTime();
			for (Polygon p : P.getPolys()) {
				p.checkEdge();
			}end = System.nanoTime();
			System.out.println("Check edges = " + (end - start));
			start = System.nanoTime();
			P.findAdjacencies();
			end = System.nanoTime();
			System.out.println("Plate adjacencies = " + (end - start));
			start = System.nanoTime();
			for (Polygon p : P.getPolys()) {
				p.calculateAllElevations();
			}end = System.nanoTime();
			System.out.println("Calc Elevs = " + (end - start));
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
