package Maps;

import java.util.ArrayList;
import java.util.Random;

import Data.Plate;

public class PlateMap {

	private ArrayList<Plate> plates;

	public PlateMap(PolyMap polyMap, int majorPlatesLower, int majorPlatesUpper, int minorPlatesLower,
			int minorPlatesUpper) {

		Random rand = new Random();
		this.plates = new ArrayList();
		int MPlates;
		
		
		if (majorPlatesUpper > majorPlatesLower) {
			MPlates = majorPlatesLower + rand.nextInt(Math.abs(majorPlatesUpper - majorPlatesLower));
		}else {
			MPlates = majorPlatesLower;
		}
		for (int i = 0; i < MPlates; i++) {
			int startPoly = rand.nextInt(polyMap.getPolys().size());
			if (!polyMap.getPolys().get(startPoly).isInPlate()) {
				
				Plate p1 = new Plate(polyMap.getPolys().get(startPoly), true);
				polyMap.getPolys().get(startPoly).setInPlate(true);
				polyMap.getPolys().get(startPoly).setPlate(p1);
				this.plates.add(p1);
				
			} else {
				i--;
			}
		}
		if (minorPlatesUpper != 0) {
			int mPlates = minorPlatesLower + rand.nextInt(Math.abs(minorPlatesUpper - minorPlatesLower));
			for (int i = 0; i < mPlates; i++) {
				int startPoly = rand.nextInt(polyMap.getPolys().size());
				if (!polyMap.getPolys().get(startPoly).isInPlate()) {

					Plate p1 = new Plate(polyMap.getPolys().get(startPoly), true);
					polyMap.getPolys().get(startPoly).setInPlate(true);
					polyMap.getPolys().get(startPoly).setPlate(p1);
					this.plates.add(p1);
					
				} else {
					i--;
				}
			}
		}

		boolean majorOnly = true;
		boolean stillUsed = true;
		while (stillUsed) {
			if (!majorOnly) {
				stillUsed = false;
			}
			for (Plate p : this.plates) {
				if (p.isMajor() || !majorOnly) {
					if (p.floodFill()) {
						stillUsed = true;
					}
				}
			}
			if (majorOnly) {
				majorOnly = false;
			} else {
				majorOnly = true;
			}
		}for (int i = 0; i < 100; i ++) {
			if (!majorOnly) {
				stillUsed = false;
			}
			for (Plate p : this.plates) {
				if (p.isMajor() || !majorOnly) {
					if (p.floodFill()) {
						stillUsed = true;
					}
				}
			}
			if (majorOnly) {
				majorOnly = false;
			} else {
				majorOnly = true;
			}
		}
		
		for (Plate p : plates) {
			p.findCentroid();
		}

	}

	public ArrayList<Plate> getPlates() {
		return plates;
	}

	public void setPlates(ArrayList<Plate> plates) {
		this.plates = plates;
	}
}
