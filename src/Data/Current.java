package Data;

import java.util.ArrayList;
import java.util.Random;

import Main.WorldConstraints;

public class Current extends Weather {

	protected Area area;
	
	private float temp;
	private int life;
	
	public Current (Area a) {
		
		this.life = WorldConstraints.timeToLive;
		this.temp = a.getOceanTemp();
		this.area = a;
		
	}
	@Override
	public void walk () {
		System.out.println("New walk from area " + this.area.toString());
		Random rand = new Random();
		ArrayList<Float> prefs = this.getPrefList();
		System.out.println("Local prefs = " + prefs);
		if (prefs == null) {
			System.out.println("Null prefs");
			return;
		}if (prefs.size() == 0) {
			System.out.println("No prefs");
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
	
	protected ArrayList<Float> getPrefList () {
		return this.area.getOceanPrefs();
	}
	
	@Override
	protected int findDirection (float dir, ArrayList<Float> prefs, int checked) {
		if (checked == prefs.size()) {
			return (Integer) null;
		}
		if (dir - prefs.get(checked) <= 0) {
			return checked;
		}return findDirection (dir - prefs.get(checked), prefs, checked + 1) ;
	}
	
	protected void moveInto () {
		
		float t;
		t = area.getOceanTemp();
		area.setOceanTemp((9 * t + this.temp)/10);
		this.temp = (9 * this.temp + t)/10;
		
	}
	
}
