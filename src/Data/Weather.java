package Data;

import java.util.ArrayList;
import java.util.Random;

public class Weather {

	protected Area area;
	private int life;
	
	public void walk () {
		System.out.println("New walk from area " + this.area.toString());
		Random rand = new Random();
		ArrayList<Float> prefs = this.getPrefList();
		System.out.println("Local prefs = " + prefs.toString());
		if (prefs.size() == 0 || prefs == null) {
			System.out.println("No prefs");
			return;
		}
		float dir = rand.nextFloat();
		try {
		this.area = this.area.getAdjacencies().get(findDirection(dir, prefs, 0));
		}catch (IndexOutOfBoundsException ex) {
			System.out.println("Couldn't find direction.");
			return;
		}
		System.out.println("Moving into " + this.area);
		this.moveInto();
		this.life --;
		System.out.println("Life left = " + this.life);
		if (this.life > 0) {
			this.walk();
		}System.out.println("Back up stack");
	}
	
	private ArrayList<Float> getPrefList () {
		// override this based on type
		System.out.println("Super");
		return null;
	}protected int findDirection (float dir, ArrayList<Float> prefs, int checked) {
		if (checked == prefs.size()) {
			return (Integer) null;
		}
		if (dir - prefs.get(checked) <= 0) {
			return checked;
		}return findDirection (dir - prefs.get(checked), prefs, checked + 1) ;
	}
	
	private void moveInto () {
		// override
		return;
	}
	
}
