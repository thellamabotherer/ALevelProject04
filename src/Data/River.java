package Data;

import java.util.ArrayList;

public class River {

	private float water = 0;
	private AreaSide current; // pun totally intended
	private float endHeight;
	private ArrayList<AreaSide> path = new ArrayList();;
	private River end; // null if ocean or lake

	public River(Area a) {

		a.setLake(true);

		// make the river object
		// put in side between this poly and lowest adj
		// System.out.println("Making a river");

		ArrayList<Area> route = a.routeToSea(50);

		for (int i = 1; i < route.size() - 1; i++) {
			for (AreaSide s : route.get(i).routeAround(route.get(i - 1), route.get(i + 1))) {
				s.setR(this);
				path.add(s);
			}
		}

		// if none lower make lake ^^

		// absorb surrounding water

		// start trying to flow

	}

	public void flow() {

		// find easiest path to the sea (non-optimal and quick)

		System.out.println("----------------------");
		System.out.println(current.getEnd());
		System.out.println(current.getA2().getAdjacencies());
		System.out.println("----------------------");

		ArrayList<Area> route = current.getEnd().routeToSea(100);
		System.out.println(current.getA2().getAdjacencies());
		System.out.println(route.get(0));

		System.out.println("----------------------");
		System.out.println("----------------------");

		// current.getEnd().setLake(true);

		for (AreaSide s : route.get(1).routeAround(route.get(0), route.get(1))) {
			s.setR(this);
		}

		/*
		 * for (int i = 0; i < route.size() - 1; i++) { if (i == 0) { for (AreaSide s :
		 * route.get(i).routeAround(current.getA2(), route.get(i+1))) { s.setR(this); }
		 * }else { for (AreaSide s : route.get(i).routeAround(route.get(i-1),
		 * route.get(i+1))) { s.setR(this); } } }
		 */

		// for each area in the path to next poly

		// keep adding these to the river unless we hit another river or a lake

		// if we hit another river, add the water in after that point

		// end the river

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