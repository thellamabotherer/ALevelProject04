package Data;

import Main.Window;

public class Weather {

	protected Area lastArea;
	protected Area area;
	protected int ttl;

	public Weather() {
		// I think the subclases handle this
		this.ttl = 50;
	}

	public void walk() {
		if (findNext() != null) {
			if (ttl > 0) {
				//System.out.println(ttl);
				ttl = ttl - 1;
				lastArea = area;
				area = findNext();
				changeClimate();
				walk();
			}
		}
		
	}

	protected Area findNext() {

		// subclasses will deal with this
		return null;

	}

	protected void changeClimate() {
		System.out.println("Climate change in the weather class");
	}

}
