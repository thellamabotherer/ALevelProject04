package Data;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import Main.GraphicalFunctions;
import Main.Window;
import Renderers.AreaRenderer;

public class Current extends Weather {

	protected Area lastArea;
	protected Area area;
	private float heat;
	protected int ttl;

	public Current(Area a) {

		// give the current an amount of heat
		this.area = a;
		this.heat = a.getOceanTemp() * 5;
		this.ttl = 50;
		// System.out.println("New cuurent\nheat = " + heat);
	}

	public void walk() {
		if (findNext() != null) {
			if (ttl > 0) {
				// System.out.println(ttl);
				ttl = ttl - 1;
				lastArea = area;
				area = findNext();
				changeClimate();
				walk();
			}
		}

	}

	protected Area findNext() {
		return area.getNextOcean();
	}

	protected void changeClimate() {

		area.setOceanTemp((float) (area.getOceanTemp() + (heat * 0.1)));
		// System.out.println("Temp + " + this.heat * 0.03 + ". ttl = " + ttl);
		for (Area a : area.getAdjacencies()) {
			if (a.isOcean())
				a.setOceanTemp((float) (a.getOceanTemp() + this.heat * 0.1));
			else
				area.setOceanTemp((float) (area.getOceanTemp() + heat * 0.1));
		}
		this.heat = (float) (this.heat * 0.6);
	}

}
