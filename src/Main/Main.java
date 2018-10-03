package Main;

import Maps.MeshMap;
import Maps.TestMap;
import Renderers.MeshRenderer;
import Renderers.TestRenderer;

public class Main {

	public static int WIDTH = 1000;
	public static int HEIGHT = 800;
	
	public static void main (String args []) {
		
		Window window = new Window (WIDTH, HEIGHT, "Test Window");
		Instance instance = new Instance (60, window);
		
		TestMap testMap = new TestMap (WIDTH, HEIGHT, (100));
		/*TestRenderer testRenderer = new TestRenderer();
		while (instance.run()) {
			testRenderer.draw(testMap.getSites(), window);
		}*/
		
		double start = System.nanoTime();
		
		MeshMap meshMap = new MeshMap(HEIGHT, HEIGHT, testMap.getSites());
		
		double end = System.nanoTime();
		
		MeshRenderer meshRenderer = new MeshRenderer (meshMap, window);
		
		System.out.println(end - start);
		
		while (instance.run()) {
			meshRenderer.draw();
		}
	}
	
}
