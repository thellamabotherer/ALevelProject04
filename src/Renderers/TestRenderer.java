package Renderers;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import Data.Point;
import Main.Window;

public class TestRenderer {
	
	public void draw (ArrayList<Point> sites, Window window) {
		window.clear();
		for (int i = 0; i < sites.size(); i++) {
			window.beginLineRender();
			window.addVertex(new Vector3f (sites.get(i).getX() - 1, sites.get(i).getY() - 1, 0));
			window.addVertex(new Vector3f (sites.get(i).getX() - 1, sites.get(i).getY() - 1, 0));
			window.addVertex(new Vector3f (sites.get(i).getX() + 1, sites.get(i).getY() + 1, 0));
			window.endRender();
			window.beginLineRender();
			window.addVertex(new Vector3f (sites.get(i).getX() + 1, sites.get(i).getY() - 1, 0));
			window.addVertex(new Vector3f (sites.get(i).getX() - 1, sites.get(i).getY() + 1, 0));
			window.addVertex(new Vector3f (sites.get(i).getX() + 1, sites.get(i).getY() - 1, 0));
			window.endRender();
		}
		
	}

}
