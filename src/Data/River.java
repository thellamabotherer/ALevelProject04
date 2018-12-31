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
			current.setR(this);
			this.water = this.water + drain(low);
			current.setWater(water);
			System.out.println(current);
			path.add(current);
			this.flow();
		}

		// if none lower make lake ^^

		// absorb surrounding water

		// start trying to flow

	}

	public void flow() {
		// find polys at both ends
		ArrayList<Area> ends = current.ends();
		Area next;
		try {
			if (ends.get(0).getAltitude() < ends.get(1).getAltitude()) {
				next = ends.get(0);
			} else {
				next = ends.get(1);
			}
			if (next.isOcean()) {
				// terminate here
				// maybe pump a bit of silt into the ocean for a nice delta?
			} else if (next.getAltitude() > current.a1.getAltitude() && next.getAltitude() > current.a2.getAltitude()) {
				if (current.a1.getAltitude() < current.a2.getAltitude()) {
					current.a1.setLake(true);
				}else {
					current.a2.setLake(true);
				}
			} else {
				// find the next edge 
				if (current.a1.getAltitude() < current.a2.getAltitude()) {
					current = current.a1.sideBetween(next);
				}else {
					current = current.a2.sideBetween(next);
				}
				current.setR(this);
				// drain new water 
				this.water = this.water + drain(next);
				current.setWater(this.water);
				//this.flow();
				
			}
		} catch (NullPointerException ex) {
			System.out.println("No next destintion for river");
			// termintate river here
		}
		// if sea then terminate
		// else if both higher then form lake on lowest adj and flood based on amount of
		// water
		// else flow into edge between lower end poly and lowest adj poly
		// absord more water
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
}
