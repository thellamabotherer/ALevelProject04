package Renderers;

import org.lwjgl.util.vector.Vector3f;

import Main.Window;
import Maps.MeshMap;

public class MeshRenderer {
	
	private Window window;
	private MeshMap map;
	
	public MeshRenderer (MeshMap map, Window window) {
		this.map = map;
		this.window = window;		
	}
	
	public void draw() {
		
		
		for (int i = 0; i < this.map.getSites().size(); i++) {
			window.beginLineRender();
			window.addVertex(new Vector3f ((float) this.map.getSites().get(i).getX() - 1, (float) this.map.getSites().get(i).getY() - 1, (float)0));
			window.addVertex(new Vector3f ((float)this.map.getSites().get(i).getX() - 1, (float)this.map.getSites().get(i).getY() - 1, 0));
			window.addVertex(new Vector3f ((float)this.map.getSites().get(i).getX() + 1, (float)this.map.getSites().get(i).getY() + 1, 0));
			window.endRender();
			window.beginLineRender();
			window.addVertex(new Vector3f ((float)this.map.getSites().get(i).getX() + 1, (float)this.map.getSites().get(i).getY() - 1, 0));
			window.addVertex(new Vector3f ((float)this.map.getSites().get(i).getX() - 1, (float)this.map.getSites().get(i).getY() + 1, 0));
			window.addVertex(new Vector3f ((float)this.map.getSites().get(i).getX() + 1, (float)this.map.getSites().get(i).getY() - 1, 0));
			window.endRender();
		}
		
		for (int i = 0; i < this.map.getEdges().size(); i++) {
			
			window.beginLineRender();
			window.addVertex(new Vector3f ((float)this.map.getEdges().get(i).getStart().getX(),(float)this.map.getEdges().get(i).getStart().getY(),(float)0));
			window.addVertex(new Vector3f ((float)this.map.getEdges().get(i).getEnd().getX(),(float)this.map.getEdges().get(i).getEnd().getY(),(float)0));
			window.addVertex(new Vector3f ((float)this.map.getEdges().get(i).getStart().getX(),(float)this.map.getEdges().get(i).getStart().getY(),(float)0));
			window.endRender();
		}
	}

}