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
			current.setR(this);
			this.water = this.water + drain(low);
			current.setWater(water);
			System.out.println(current);
			path.add(current);
			//this.flow();
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
			
			// if end0 lowest
			if (ends.get(0).getAltitude() < ends.get(1).getAltitude()) {	
				// find the next edge 
				next = ends.get(0);
				if (current.a1.getAltitude() < current.a2.getAltitude()) {
					current = current.a1.sideBetween(next);
				}else {
					current = current.a2.sideBetween(next);
				}
				// check if next edge  is in the path somewhere
				boolean found = false;
				for (int i = 0; i < path.size(); i++) {
					if (found) {
						path.remove(i);
						i--;
					}else {
						if (path.get(i) == current) {
							if (current.a1.getAltitude() < current.a2.getAltitude()) {
								current.a1.setLake (true);
							}else {
								current.a2.setLake(true);
							}
						}found = true;
					}
				}
					// if so, make a lake 
				// check if this'll go into the sea
				if (current.a1.isOcean() || current.a2.isOcean()) {
					// terminate river 
				}
				// check if this'll go into another river
				else if (current.hasRiver()) {
					// add water to this river (everything down the path from here)
					current.getR().addWater (current, this.water) ;
					// terminate river 
				}
				// if carves > 0 and next not too much higher 
				if (carves > 0) {
					carves --;
					// decrement carves
					// make the next poly lower to let river through 
				}
				// else 
					// start a new lake 
			// same but for end1 when that's lower 
					
				
			
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

	private void addWater(AreaSide current2, float water2) {
		boolean f = false;
		for (AreaSide s : this.path) {
			if (f) {
				s.setWater(s.getWater() + water2);
			}else if (current2 == s) {
				f = true;
				s.setWater (s.getWater() + water2) ;
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
