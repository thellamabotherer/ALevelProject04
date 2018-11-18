package Renderers;

import java.util.Random;

import org.lwjgl.util.vector.Vector4f;

import Data.Area;
import Data.Edge;
import Main.ColourPalette;
import Main.Window;
import Main.WorldConstraints;
import Maps.AreaMap;

public class AreaRenderer {

	private AreaMap areas;
	private Window window;

	public AreaRenderer(AreaMap map, Window window) {
		this.areas = map;
		this.window = window;
	}

	// render areas based on height

	public void drawHeights() {
		for (Area a : this.areas.getAreas()) {

			a.getPoly().draw(window, getHeat((a.getAltitude() + 1) / 2));
			for (Edge e : a.getPoly().getEdges()) {
				if (e.getLeftSite().getPoly().getPlate() != e.getRightSite().getPoly().getPlate()) {
					window.beginLineRender();
					window.changeColour(new Vector4f(1, 1, 1, 0));
					window.addVertex(e.getStart().coords());
					window.addVertex(e.getEnd().coords());
					window.addVertex(e.getStart().coords());

				}
			}

		}
	}

	// render areas based on simple terrain

	public void setSimpleTerrain() {
		Random rand = new Random();
		for (Area a : this.areas.getAreas()) {
			if (a.getLatitude() < (float) (WorldConstraints.HEIGHT * 0.1) || a.getLatitude() > (float) (WorldConstraints.HEIGHT * 0.9)) {
				a.setColour(ColourPalette.polar[rand.nextInt(4)]);
			} else {
				if (a.getAltitude() < -0.3) {
					a.setColour(ColourPalette.deepSea[rand.nextInt(4)]);
				}else if (a.getAltitude() < 0) {
					a.setColour(ColourPalette.shallowSea[rand.nextInt(4)]);
				}else if (a.getAltitude() < 0.1) {
					a.setColour(ColourPalette.coast[rand.nextInt(4)]);
				}else if (a.getAltitude() < 0.4) {
					a.setColour(ColourPalette.plains[rand.nextInt(4)]);
				}else if (a.getAltitude() < 0.8) {
					a.setColour(ColourPalette.hills[rand.nextInt(4)]);
				}else {
					a.setColour(ColourPalette.mountains[rand.nextInt(4)]);
				}
			}
		}

	}public void drawSimpleTerrain () {
		for (Area a : this.areas.getAreas()) {
			a.getPoly().draw(window, a.getColour());
		}
	}

	private Vector4f getHeat(float val) { // takes val from 0 to 1, returns heatmap colour
		float r;
		float g;
		float b;
		if (val < 0.25) { // 0 0 153 to 0 102 204
			r = (float) ((((val - 0.00) * 4) * ((float) 0 / (float) 255)
					+ ((0.25 - val) * 4) * ((float) 0 / (float) 255)) * 0.7);
			g = (float) ((((val - 0.00) * 4) * ((float) 102 / (float) 255)
					+ ((0.25 - val) * 4) * ((float) 0 / (float) 255)) * 0.7);
			b = (float) ((((val - 0.00) * 4) * ((float) 204 / (float) 255)
					+ ((0.25 - val) * 4) * ((float) 153 / (float) 255)) * 0.7);
			return new Vector4f(r, g, b, 0);
		} else if (val < 0.5) { // 0 102 204 to 0 153 0
			r = (float) ((((val - 0.25) * 4) * ((float) 0 / (float) 255)
					+ ((0.50 - val) * 4) * ((float) 0 / (float) 255)) * 0.7);
			g = (float) ((((val - 0.25) * 4) * ((float) 153 / (float) 255)
					+ ((0.50 - val) * 4) * ((float) 102 / (float) 255)) * 0.7);
			b = (float) ((((val - 0.25) * 4) * ((float) 0 / (float) 255)
					+ ((0.50 - val) * 4) * ((float) 204 / (float) 255)) * 0.7);
			return new Vector4f(r, g, b, 0);
		} else if (val < 0.75) { // 0 153 0 to 255 255 51
			r = (float) ((((val - 0.50) * 4) * ((float) 255 / (float) 255)
					+ ((0.75 - val) * 4) * ((float) 0 / (float) 255)) * 0.7);
			g = (float) ((((val - 0.50) * 4) * ((float) 255 / (float) 255)
					+ ((0.75 - val) * 4) * ((float) 153 / (float) 255)) * 0.7);
			b = (float) ((((val - 0.50) * 4) * ((float) 51 / (float) 255)
					+ ((0.75 - val) * 4) * ((float) 0 / (float) 255)) * 0.7);
			return new Vector4f(r, g, b, 0);
		} else {// 255 255 51 to 255 0 0
			r = (float) ((((val - 0.75) * 4) * ((float) 255 / (float) 255)
					+ ((1 - val) * 4) * ((float) 255 / (float) 255)) * 0.7);
			g = (float) ((((val - 0.75) * 4) * ((float) 0 / (float) 255)
					+ ((1 - val) * 4) * ((float) 255 / (float) 255)) * 0.7);
			b = (float) ((((val - 0.75) * 4) * ((float) 0 / (float) 255) + ((1 - val) * 4) * ((float) 51 / (float) 255))
					* 0.7);
			return new Vector4f(r, g, b, 0);
		}
	}

}