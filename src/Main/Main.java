package Main;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Data.Matrix2d;
import Data.Plate;
import Data.Point;
import Data.Polygon;
import Maps.AreaMap;
import Maps.HeightMap;
import Maps.MeshMap;
import Maps.PlateMap;
import Maps.PolyMap;
import Maps.TestMap;
import Renderers.AreaRenderer;
import Renderers.HeightRenderer;
import Renderers.MeshRenderer;
import Renderers.PlateRenderer;
import Renderers.TestRenderer;

public class Main {

	public static int WIDTH = WorldConstraints.WIDTH;
	public static int HEIGHT = WorldConstraints.HEIGHT;

	public static Window window;
	public static Instance instance;

	public static TestMap testMap;
	public static MeshMap meshMap;
	public static PolyMap polyMap;
	public static PlateMap plateMap;
	public static HeightMap heightMap;
	public static AreaMap areaMap;

	public static TestRenderer testRenderer;
	public static MeshRenderer meshRenderer;
	public static PlateRenderer plateRenderer;
	public static HeightRenderer heightRenderer;
	public static AreaRenderer areaRenderer;

	public static void main(String args[]) {

		GraphicalFunctions.setup();
		newWindow(

				WIDTH, // width
				HEIGHT, // height
				"Test Map", // window name
				30 // frame rate

		);

		instance = new Instance(30, window);

		double start = System.nanoTime();

		newMap(

				10000, // number of sites
				2, // number of passes of the lloyd relaxation
				(float) 0.5, // strength of each relaxation
				20, 30, // range of possible major plate numbers
				0, 0 // range of possible minor plate numbers
		);

		double end = System.nanoTime();
		System.out.println(end - start);

		// int active = 4;

		int active = 7;
		// for (int active = 5; active < 7; active++) {
		while (instance.run()) {
			// checkInput();
			switch (active) {
			case 0:
				testRenderer.draw(testMap.getSites(), window);
				break;
			case 1:
				meshRenderer.draw();
				break;
			case 2:
				plateRenderer.draw();
				break;
			case 3:
				areaRenderer.drawHeights();
				break;
			case 4:
				areaRenderer.drawSimpleTerrain();
				break;
			case 5:
				areaRenderer.drawCurrents();
				/* I want to */ break; // free
			case 6:
				areaRenderer.drawWinds();
				break;
			case 7:
				areaRenderer.drawSeaTemp();
				break;
			case 8 :
				areaRenderer.drawAirTemp();
				break;
			case 9 :
				areaRenderer.drawRainfall();
				break;
			}
			if (checkInput() != -1) {
				active = checkInput();
			}
		}
		// }
	}

	private static int checkInput() {
		// keyboard handlers look tricky, do this later
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) { // test map
			return 0;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_V)) { // voronoi map
			return 1;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_P)) { // plate map
			return 2;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_H)) { // area map heights
			return 3;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_T)) { // simple terrain map
			return 4;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_C)) { // ocean current map
			return 5;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_W)) { // wind map
			return 6;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_Y)) { // ocean heat
			return 7;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_U)) { // air heat
			return 8;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_I)) { // rainfall
			return 9;
		}
		return -1;

	}

	private static void newWindow(int WIDTH, int HEIGHT, String name, int fps) {

		window = new Window(WIDTH, HEIGHT, name);
		instance = new Instance(fps, window);

	}

	private static void newMap(int numSites, int numRelaxations, float relaxDist, int bigPlateMax, int bigPlateMin,
			int smallPlateMax, int smallPlateMin) {
		
		double start;
		double end;
		
		start = System.nanoTime();
		testMap = new TestMap(WIDTH, HEIGHT, numSites);
		end = System.nanoTime();
		System.out.println("Test map takes " + (end - start));
		
		start = System.nanoTime();
		meshMap = new MeshMap(WIDTH, HEIGHT, testMap.getSites(), window, false, testMap.getRoot());
		end = System.nanoTime();
		System.out.println("Mesh map takes " + (end - start));
		
		start = System.nanoTime();
		for (int i = 0; i < numRelaxations; i++) {
			polyMap = new PolyMap(meshMap.getSites().get(0), meshMap.getEdges(), meshMap.getSites());
			testMap = new TestMap(polyMap.relax(relaxDist));
			meshMap = new MeshMap(WIDTH, HEIGHT, testMap.getSites(), window, false, testMap.getRoot());
		}end = System.nanoTime();
		System.out.println("Relaxed map takes " + (end - start));
		
		start = System.nanoTime();
		polyMap = new PolyMap(meshMap.getSites().get(0), meshMap.getEdges(), meshMap.getSites());
		end = System.nanoTime();
		System.out.println("Poly map takes " + (end - start));
		
		start = System.nanoTime();
		plateMap = new PlateMap(polyMap, bigPlateMin, bigPlateMax, smallPlateMin, smallPlateMax);
		end = System.nanoTime();
		System.out.println("Plate map takes " + (end - start));
		
		start = System.nanoTime();
		heightMap = new HeightMap(plateMap.getPlates());
		end = System.nanoTime();
		System.out.println("Height map takes " + (end - start));
		
		testRenderer = new TestRenderer();
		meshRenderer = new MeshRenderer(meshMap, window);
		plateRenderer = new PlateRenderer(plateMap, window);
		heightRenderer = new HeightRenderer(window, heightMap);

		heightRenderer.drawSimpleTerrain();
		start = System.nanoTime();
		areaMap = new AreaMap(heightMap, window, heightRenderer);
		end = System.nanoTime();
		System.out.println("Area map takes " + (end - start));
		
		areaRenderer = new AreaRenderer(areaMap, window);
		areaRenderer.setSimpleTerrain();

	}
}