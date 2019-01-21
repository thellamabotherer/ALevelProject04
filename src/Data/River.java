package Data;

import java.util.ArrayList;

public class River {

	private float water = 0;
	private AreaSide current; // pun totally intended
	private float endHeight;
	private ArrayList<AreaSide> path = new ArrayList();;
	private River end; // null if ocean or lake

	public River(Area a) {

		// make the river object
		// put in side between this poly and lowest adj
		// System.out.println("Making a river");

		ArrayList<Area> route = a.routeToSea(50);
		path.add(a.getSides().get(0));
		for (int i = 1; i < route.size() - 1; i++) {
			AreaSide l = path.get(path.size()-1);
			AreaSide b;
			if (this.path.size() < 2) {
				b = null;
			}else {
				b = path.get(path.size()-2);
			}
			for (AreaSide s : route.get(i).routeAround(route.get(i - 1), route.get(i + 1), l, b)) {
				s.setR(this);
				path.add(s);
			}
		}
		

		// if none lower make lake ^^

		// absorb surrounding water

		// start trying to flow

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