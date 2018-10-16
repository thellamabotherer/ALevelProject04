package Main;

import Data.Point;
import Maps.MeshMap;
import Maps.PlateMap;
import Maps.PolyMap;
import Maps.TestMap;
import Renderers.MeshRenderer;
import Renderers.PlateRenderer;
import Renderers.TestRenderer;

public class Main {

	public static int WIDTH = 1920;
	public static int HEIGHT = 1080;	
	
	public static Window window;
	public static Instance instance;
	
	public static TestMap testMap;
	public static MeshMap meshMap;
	public static PolyMap polyMap;
	public static PlateMap plateMap;
	
	public static MeshRenderer meshRenderer;	
	public static PlateRenderer plateRenderer;
	
	public static void main (String args []) {		
		
		newMap (
				WIDTH, // width
				HEIGHT, // height
				"Test Map", // window name
				60, // frame rate
				10000, // number of sites
				2, // number of passes of the lloyd relaxation
				(float) 0.5, // strength of each relaxation
				3,   5, // range of possible major plate numbers
				3,   5  // range of possible minor plate numbers
				);
		
		meshRenderer = new MeshRenderer (meshMap, window);
		plateRenderer = new PlateRenderer(plateMap, window);
		while (instance.run()) {
			meshRenderer.draw();
			plateRenderer.draw();
		}
		
		
	}
	
	private static void newMap (int WIDTH, int HEIGHT, String name, int fps, int numSites, int numRelaxations, float relaxDist, int bigPlateMax, int bigPlateMin, int smallPlateMax, int smallPlateMin) {
		
		window = new Window (WIDTH, HEIGHT, name);
		instance = new Instance (fps, window);
		testMap = new TestMap (WIDTH, HEIGHT, numSites);
		meshMap = new MeshMap(WIDTH, HEIGHT, testMap.getSites(), window, false, testMap.getRoot());
		
		for (int i = 0; i < numRelaxations; i++) {
			polyMap = new PolyMap (meshMap.getSites().get(0), meshMap.getEdges(), meshMap.getSites());
			testMap = new TestMap (polyMap.relax(relaxDist));
			meshMap = new MeshMap (WIDTH, HEIGHT, testMap.getSites(), window, false, testMap.getRoot());
		}
		
		polyMap = new PolyMap (meshMap.getSites().get(0), meshMap.getEdges(), meshMap.getSites());
		plateMap = new PlateMap (polyMap, bigPlateMin, bigPlateMax, smallPlateMin, smallPlateMax);
	}
	
}
