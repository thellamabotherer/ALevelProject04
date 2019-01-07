package Renderers;

import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Data.Area;
import Data.AreaSide;
import Data.Edge;
import Data.WeatherSystem;
import Main.ColourPalette;
import Main.GraphicalFunctions;
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
			if (a.getLatitude() < (float) (WorldConstraints.HEIGHT * 0.05)
					|| a.getLatitude() > (float) (WorldConstraints.HEIGHT * 0.95)) {
				a.setColour(ColourPalette.white);
			} else {
				if (a.getAltitude() < -0.7) {
					a.setColour(ColourPalette.deepBlue);
				} else if (a.getAltitude() < -0.3) {
					a.setColour(ColourPalette.middleBlue);
				} else if (a.getAltitude() < 0) {
					a.setColour(ColourPalette.lightBlue);
				} else if (a.getAltitude() < 0.05) {
					a.setColour(ColourPalette.yellow);
				} else if (a.getAltitude() < 0.3) {
					a.setColour(ColourPalette.lightGreen);
				} else if (a.getAltitude() < 0.5) {
					a.setColour(ColourPalette.deepGreen);
				} else if (a.getAltitude() < 0.8) {
					a.setColour(ColourPalette.brown);
				} else {
					a.setColour(ColourPalette.mountains2);
				}
			}
		}

	}

	public void drawSimpleTerrain() {
		for (Area a : this.areas.getAreas()) {
			if (a.isLake()) {
				a.getPoly().draw(window, ColourPalette.lightBlue);
			} else {
				a.getPoly().draw(window, a.getColour());
			}for (AreaSide s : a.getSides()) {
				if (s.hasRiver()) {
					window.beginLineRender();
					window.changeColour(ColourPalette.deepBlue);
					window.addVertex(new Vector3f ((float)s.getP1().getX(), (float)s.getP1().getY(), 0));
					window.addVertex(new Vector3f ((float)s.getP2().getX(), (float)s.getP2().getY(), 0));
					window.addVertex(new Vector3f ((float)s.getP1().getX(), (float)s.getP1().getY(), 0));
					window.endRender();
					window.beginLineRender();
					window.changeColour(ColourPalette.deepBlue);
					window.addVertex(new Vector3f ((float)s.getP1().getX(), (float)s.getP1().getY(), 0));
					window.addVertex(new Vector3f ((float)s.getP2().getX(), (float)s.getP2().getY(), 0));
					window.addVertex(new Vector3f ((float)s.getP1().getX(), (float)s.getP1().getY(), 0));
					window.endRender();
				}
			}
		}
	}

	public void drawCurrents() {
		// double start = System.nanoTime();
		for (Area a : this.areas.getAreas()) {
			a.getPoly().draw(window, a.getColour());
			window.changeColour(ColourPalette.red);
			GraphicalFunctions.drawArrow(window, new Vector2f(a.getLongditude(), a.getLatitude()), a.getCurrents(), 7);
			// for (WeatherSystem e : areas.getEpicentres()) {
			// e.draw(window);
			// s}//Display.update();
		} // double end = System.nanoTime();
			// System.out.println(end - start);
	}

	public void drawWinds() {
		// double start = System.nanoTime();
		this.drawSimpleTerrain();
		for (Area a : this.areas.getAreas()) {
			// a.getPoly().draw(window, a.getColour());
			window.changeColour(ColourPalette.red);
			GraphicalFunctions.drawArrow(window, new Vector2f(a.getLongditude(), a.getLatitude()), a.getWinds(), 7);
			// for (WeatherSystem e : areas.getEpicentres()) {
			// e.draw(window);
			// }//Display.update();
		} // double end = System.nanoTime();
			// System.out.println(end - start);
	}

	public void drawSeaTemp() {
		for (Area a : this.areas.getAreas()) {
			if (a.isOcean()) {
				a.getPoly().draw(window, getHeat(a.getOceanTemp()));
			} else {
				a.getPoly().draw(window, ColourPalette.grey);
			}
		}
	}

	public void drawAirTemp() {
		for (Area a : this.areas.getAreas()) {
			a.getPoly().draw(window, getHeat(a.getAirTemp()));
		}
	}

	public void drawRainfall() {

		for (Area a : this.areas.getAreas()) {
			if (a.isOcean()) {
				a.getPoly().draw(window, ColourPalette.grey);
			} else {
				a.getPoly().draw(window, getHeat(1 - a.getWater()));
			}
		}

	}

	public static Vector4f getHeat(float val) { // takes val from 0 to 1, returns heatmap colour
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
