package Main;

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
		
		instance = new Instance (30, window);
		
		/*Float[][] arr = new Float[2][4];
		arr[0][0] = (float) 1;
		arr[0][1] = (float) 0;
		arr[1][0] = (float) 0;
		arr[1][1] = (float) 1;
		arr[0][2] = (float) 1;
		arr[0][3] = (float) 9;
		arr[1][2] = (float) 6;
		arr[1][3] = (float) 1;
		
		Matrix2d<Float> shape = new Matrix2d<Float> (arr);
		Float[][] arr2 = new Float[2][2];
		arr2[0][0] = (float) 1;
		arr2[0][1] = (float) 0;
		arr2[1][0] = (float) 0;
		arr2[1][1] = (float) 1;
		Matrix2d<Float> transform = new Matrix2d<Float> (arr2);
		Matrix2d<Float> image = shape.mult(transform);
		image.print();*/
		
		
		double start = System.nanoTime();
		 
		newMap(

				10000, // number of sites
				2, // number of passes of the lloyd relaxation
				(float) 0.5, // strength of each relaxation
				10, 20, // range of possible major plate numbers
				0, 0 // range of possible minor plate numbers
		);

		
		
		double end = System.nanoTime();
		System.out.println(end - start);

		//int active = 4;
		
		
		
		for (int active = 5; active < 7; active++) {
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
	 /*I want to*/  break; // free 
				case 6:
					areaRenderer.drawWinds();
					break;
				}
			}
		}
	}

	private static void checkInput() {
		// keyboard handlers look tricky, do this later
	}

	private static void newWindow(int WIDTH, int HEIGHT, String name, int fps) {

		window = new Window(WIDTH, HEIGHT, name);
		instance = new Instance(fps, window);

	}

	private static void newMap(int numSites, int numRelaxations, float relaxDist, int bigPlateMax, int bigPlateMin,
			int smallPlateMax, int smallPlateMin) {

		testMap = new TestMap(WIDTH, HEIGHT, numSites);
		meshMap = new MeshMap(WIDTH, HEIGHT, testMap.getSites(), window, false, testMap.getRoot());

		for (int i = 0; i < numRelaxations; i++) {
			polyMap = new PolyMap(meshMap.getSites().get(0), meshMap.getEdges(), meshMap.getSites());
			testMap = new TestMap(polyMap.relax(relaxDist));
			meshMap = new MeshMap(WIDTH, HEIGHT, testMap.getSites(), window, false, testMap.getRoot());
		}

		polyMap = new PolyMap(meshMap.getSites().get(0), meshMap.getEdges(), meshMap.getSites());
		plateMap = new PlateMap(polyMap, bigPlateMin, bigPlateMax, smallPlateMin, smallPlateMax);

		heightMap = new HeightMap(plateMap.getPlates());
		
		testRenderer = new TestRenderer();
		meshRenderer = new MeshRenderer(meshMap, window);
		plateRenderer = new PlateRenderer(plateMap, window);
		heightRenderer = new HeightRenderer(window, heightMap);

		areaMap = new AreaMap(heightMap, window, heightRenderer);
		areaRenderer = new AreaRenderer(areaMap, window);
		areaRenderer.setSimpleTerrain();
		
		
	}
}