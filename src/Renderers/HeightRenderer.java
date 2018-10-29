package Renderers;

import Main.ColourPalette;
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
	
	public void drawHeight () {
		for (Plate P : this.heightMap.getPlates()) {
			for (Polygon p : P.getPolys()) {
				float red;
				float blue;
				if (p.getHeight() < 0) {red = 0;} else {red = p.getHeight();}
				float green = (1- Math.abs(p.getHeight()));
				if (p.getHeight() > 0) {blue = 0;} else {blue = - p.getHeight();}
				
				
				p.draw(this.window, new Vector4f(red, green, blue, (float)1));
			}
		}
	}public void drawSimpleTerrain () {
		for (Plate P : this.heightMap.getPlates()) {
			for (Polygon p : P.getPolys()) {
				if (p.getHeight() < -0.7) {
					p.draw(window, ColourPalette.deepBlue);
				} else if (p.getHeight() < -0.3) {
					p.draw(window, ColourPalette.middleBlue);
				} else if (p.getHeight() < 0) {
					p.draw(window, ColourPalette.lightBlue);
				} else if (p.getHeight() < 0.05) {
					p.draw(window, ColourPalette.yellow);
				} else if (p.getHeight() < 0.3) {
					p.draw(window, ColourPalette.lightGreen);
				} else if (p.getHeight() < 0.5) {
					p.draw(window, ColourPalette.deepGreen);
				} else if (p.getHeight() < 0.8) {
					p.draw(window, ColourPalette.brown);
				} else {
					p.draw(window, ColourPalette.white);
				}
			}
		}
	}

}
