package Data;

import java.util.ArrayList;

public class pQueue {

	ArrayList<Area> queue;

	// constructor

	public pQueue(ArrayList<Area> al) {
		queue = new ArrayList();
		// quicksort al into queue
		for (Area a : al) {
			if (decideValue(a) > 0) {
				queue.add(a);
			}
		}sortQ(queue);
	}

	public Area pop () {
		if (!isSorted(queue)) {
			//System.out.println("Isn't sorted already");
			queue = sortI (queue);
		}Area buffer = queue.get(queue.size() - 1);
		queue.remove(queue.size() -1);
		return buffer ;
	}
	
	private ArrayList<Area> sortQ (ArrayList<Area> l ) { // quicksort not good when list almost in order
		
		//System.out.println("Quick sort , start len " + l.size());
		
		Area pivot = l.get(0);
		float val = decideValue(pivot);
		ArrayList<Area> l1 = new ArrayList();
		ArrayList<Area> l2 = new ArrayList();
		
		for (int i = 1; i < l.size(); i++) {
			if (decideValue(l.get(i)) < val) {
		      l1.add(l.get(i));
			} else {
		      l2.add(l.get(i));
			}
		}
	  if (l1.size() > 1) {
		  //System.out.println("l1 len " + l1.size());
		  l1 = sortQ(l1);
	  }if (l2.size() > 1) {
		  //System.out.println("l2 len " + l2.size());
		  l2 = sortQ(l2);
	  }
	  ArrayList<Area> l3 = new ArrayList();
	  for (Area a : l1) {
	    l3.add(a);
	  }
	  l3.add(pivot);
	  for (Area a : l2) {
	    l3.add(a);
	  }
	  
	  //System.out.println("Quick sort , end len " + l3.size());
	  
	  return l3;
	}
	
	private static ArrayList<Area> sortI (ArrayList<Area> l) { // insertion sort better when list almost in order
		
		System.out.println("Insertion sort , start len " + l.size());
		
		ArrayList<Area> sorted = new ArrayList();
		sorted.add(l.get(0));
		for (int i = 1; i < l.size(); i++) {
			if (decideValue(l.get(i)) >= decideValue(sorted.get(sorted.size()-1))) {
				sorted.add(l.get(i));
			}else {
				for (int j = 0; j < sorted.size(); j++) {
					if (decideValue(l.get(i)) <= decideValue(sorted.get(j))) {
						sorted.add(j, l.get(i));
						j = sorted.size();
					}
				}
			}
		}
		
		System.out.println("Insertion sort , end len " + sorted.size());
		return sorted;
	}
				  
	private static float decideValue(Area a) {
		if (a.getAltitude() <= 0) {
			return 0;
		}return a.getRiverWeight();
	}

	// push

	// pop

	public boolean isEmpty () {
		System.out.println(queue.size());
		if (queue.size() < 1) {
			return true;
		}return false;
	}public int len () {
		return queue.size();
	}private boolean isSorted (ArrayList<Area> l) {
		for (int i = 1; i < l.size(); i++) {
			if (decideValue(l.get(i)) < decideValue(l.get(i-1))) {
				return false;
			}
		}return true;
	}
	
}
