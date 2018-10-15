package Renderers;

import Data.Plate;
import Main.Window;
import Maps.PlateMap;

public class PlateRenderer {
	
	private PlateMap plateMap;
	private Window window;
	
	public PlateRenderer(PlateMap plateMap, Window window) {
		this.plateMap = plateMap;
		this.window = window;
	}
	
	public void draw () {
		for (Plate p : this.plateMap.getPlates()) {
			p.draw(window);
		}
	}

}
