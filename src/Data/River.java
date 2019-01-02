package Data;

import java.util.ArrayList;

public class River {

	private float water = 0;
	private AreaSide current; // pun totally intended
	private float endHeight;
	private ArrayList<AreaSide> path = new ArrayList();;
	private River end; // null if ocean or lake
	private int carves = 10;
	private River downstream ;
	private AreaSide downstreamNode ;
	private boolean valid = true;
	
	public River(Area a) {
		System.out.println("New river");
		
		
		// start with polygon 
		
		// find lowest adj
		Area l = a.getAdjacencies().get(0);
		for (Area b : a.getAdjacencies()) {
			if (b.getAltitude() < l.getAltitude()) {
				l = b;
			}
		}
		// find first edge 
		current = a.sideBetween(l);
		// add that edge to the river 
		current.setR(this);
		path.add(current);
		this.water = this.water + drain(current.getA1()) + drain(current.getA2());
		current.setWater(this.water);
		
		int tries = 100;
		while (flow() && tries > 0) {
			tries -- ;
			water = water + drain(current.a1) + drain(current.a2);
		}//this.changeWeights();
		if (!valid) {
			// delete
		}

	}

	public boolean flow () {
		//System.out.println("Flow");
		boolean flowing = true;
		
		// find polys at each end of current edge 
		
		// discard the poly we just came from 
		
		// if next poly ocean or lake
			// end river here 
		
		// find the two edges we can go to 
		
		// find the polys at the ends of each of those 
		
		// take edge between end of this edge and the lower of these 
		
		// if that poly lower than the current one 
			// if this river has enough water 
				// make lake  on lower poly 
				// if one of poly's neighbours is lower than both of those next to the river in 
					// make a new river between that and it's lowest neighbour 
		
		
		// if no river in this edge 
			// keep flowing 
		
		// if another river in this edge 
			// add this river's water to that river 
			// end this river
		
		// if back into this river
			// delete this river 

		
		return flowing ;
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
		}
	}

}
