package Data;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Main.ColourPalette;
import Main.GraphicalFunctions;
import Main.Window;
import Renderers.AreaRenderer;
import Renderers.HeightRenderer;

public class Cloud extends Weather {

	protected Area lastArea;
	protected Area area;
	private float heat;
	protected int ttl;

	public Cloud(Area a) {

		// give the current an amount of heat
		this.area = a;
		this.heat = a.getAirTemp() * 5;
		// System.out.println(a.getAirTemp());
		this.ttl = 40;
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
		return area.getNextAir();
	}

	protected void changeClimate() {

		// Window window = Main.Main.window;

		area.setAirTemp((float) (area.getAirTemp() + (heat * 0.1)));
		// System.out.println("Temp + " + this.heat * 0.03 + ". ttl = " + ttl);
		for (Area a : area.getAdjacencies()) {
			a.setAirTemp((float) (a.getAirTemp() + this.heat * 0.1));
		}
		this.heat = (float) (this.heat * 0.6);

	}

}