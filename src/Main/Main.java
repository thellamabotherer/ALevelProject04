package Main;

import Data.Point;
import Maps.MeshMap;
import Maps.TestMap;
import Renderers.MeshRenderer;
import Renderers.TestRenderer;

public class Main {

	public static int WIDTH = 1920;
	public static int HEIGHT = 1080;
	
	public static int firstIf = 0;
	public static int secondIf = 0;
	public static int thirdIf = 0;
	public static int fourthIf = 0;
	public static int fifthIf = 0;
	public static int sixthIf = 0;
	public static int noIfs = 0;
	
	
	public static void main (String args []) {		
		
		Window window = new Window (WIDTH, HEIGHT, "Test Window");
		Instance instance = new Instance (60, window);
		
		double start = System.nanoTime();
		
		TestMap testMap = new TestMap (WIDTH, HEIGHT, (1000));
		
		/*TestRenderer testRenderer = new TestRenderer();
		while (instance.run()) {
			testRenderer.draw(testMap.getSites(), window);
		}*/
		
		
		
		MeshMap meshMap = new MeshMap(HEIGHT, HEIGHT, testMap.getSites(), window, false);
		
		double end = System.nanoTime();
		
		MeshRenderer meshRenderer = new MeshRenderer (meshMap, window);
		
		System.out.println(end - start);
		
		while (instance.run()) {
			meshRenderer.draw();
		}
		
		// step 2, make polygons out of all edges that share a common point and draw some of the polys in different colours to see if they work 
		
		
	}
	
}
