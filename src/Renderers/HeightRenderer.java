package Renderers;

import Main.Window;

import org.lwjgl.util.vector.Vector4f;

import Data.Plate;
import Data.Polygon;
import Maps.HeightMap;
import Maps.PlateMap;

public class HeightRenderer {
	
	private HeightMap heightMap;
	private Window window;
	
	public HeightRenderer (Window window, HeightMap heightMap) {
		this.heightMap = heightMap;
		this.window = window;
	}
	
	public void draw () {
		for (Plate P : this.heightMap.getPlates()) {
			for (Polygon p : P.getPolys()) {
				p.draw(this.window, new Vector4f((float)0, (float)0, (float) ((float)p.getHeight()/2), (float)1));
			}
		}
	}

}
