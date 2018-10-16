package Renderers;

import Main.Window;

import org.lwjgl.util.vector.Vector4f;

import Data.Plate;
import Data.Polygon;
import Maps.PlateMap;

public class HeightRenderer {
	
	private PlateMap plateMap;
	private Window window;
	
	public HeightRenderer (Window window, PlateMap plateMap) {
		this.plateMap = plateMap;
		this.window = window;
	}
	
	public void draw () {
		for (Plate P : this.plateMap.getPlates()) {
			for (Polygon p : P.getPolys()) {
				p.draw(this.window, new Vector4f((float) (p.getHeight()/2.5), 0, 0, 1));
			}
		}
	}

}
