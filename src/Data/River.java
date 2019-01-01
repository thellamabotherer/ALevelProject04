package Data;

import java.util.ArrayList;

public class River {

	private float water = 0;
	private AreaSide current; // pun totally intended
	private float endHeight;
	private ArrayList<AreaSide> path = new ArrayList();;
	private River end; // null if ocean or lake
	private int carves = 10;

	public River(Area a) {
		// make the river object
		// put in side between this poly and lowest adj
		System.out.println("Making a river");
		float lowest = a.getAltitude();
		Area low = a;
		for (Area r : a.getAdjacencies()) {
			if (r.getAltitude() < lowest) {
				low = r;
				lowest = low.getAltitude();
			}
		}
		if (low == a) {
			// make a lake here
			a.setLake(true);
			// still go about draining the water though

			this.water = this.water + drain(low);

			// some day I've got to work out how to make lakes bigger than just a single
			// tile but it'll do for now
		} else {
			current = low.sideBetween(a);
			this.flow();
		}

		// if none lower make lake ^^

		// absorb surrounding water

		// start trying to flow

	}

	public void flow() {
		// find polys at both ends
		

	}

	private void addWater(AreaSide current2, float water2) {
		boolean f = false;
		for (AreaSide s : this.path) {
			if (f) {
				s.setWater(s.getWater() + water2);
			} else if (current2 == s) {
				f = true;
				s.setWater(s.getWater() + water2);
			}
		}

	}

	private static float drain(Area a) {
		float f = a.getActiveWater();
		if (f == 0) {
			return 0;
		}
		a.setActiveWater(0);
		for (Area b : a.getAdjacencies()) {
			if (b.getAltitude() > a.getAltitude()) {
				f = f + drain(b);
				b.setActiveWater(0);
			}
		}
		return f;
	}

	public void changeWeights() {
		for (AreaSide s : this.path) {
			s.a1.rivWeightFlood(1);
			s.a2.rivWeightFlood(1);
		}
	}

}
